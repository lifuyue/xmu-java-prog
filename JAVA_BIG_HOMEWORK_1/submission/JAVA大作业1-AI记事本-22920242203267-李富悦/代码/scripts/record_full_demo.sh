#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
SRC_DIR="$PROJECT_DIR/src"
BIN_DIR="$PROJECT_DIR/bin"
VIDEO_DIR="$PROJECT_DIR/video"
DEMO_FILE="$VIDEO_DIR/demo-note.txt"
OUTPUT_FILE="$VIDEO_DIR/记事本功能演示.mov"
WINDOW_RECORDING_LEAD_IN_MS="${WINDOW_RECORDING_LEAD_IN_MS:-4500}"
WINDOW_ID_TIMEOUT_SECONDS="${WINDOW_ID_TIMEOUT_SECONDS:-12}"

JAVA_BIN="${JAVA_BIN:-java}"
JAVAC_BIN="${JAVAC_BIN:-javac}"
PACE="slow"
MODE="record"
RECORD_SECONDS=""

usage() {
  cat <<'USAGE'
Usage:
  scripts/record_full_demo.sh [--dry-run] [--drive-only] [--pace slow|normal|fast|1.2] [--seconds N] [--output FILE]

Modes:
  --dry-run     只打印演示时间线和建议录屏秒数，不启动窗口，不录屏。
  --drive-only 只启动并自动演示程序，不录屏。用于先看节奏。

Defaults:
  --pace slow   默认慢速节奏，方便老师看清每个功能。
  output        video/记事本功能演示.mov
  record        真实录制时只录制 AI 记事本窗口，不录制整个屏幕。

Before real recording:
  macOS 可能需要给当前终端或 Codex 打开“屏幕录制”权限。
USAGE
}

while [[ $# -gt 0 ]]; do
  case "$1" in
    --dry-run)
      MODE="dry-run"
      shift
      ;;
    --drive-only)
      MODE="drive-only"
      shift
      ;;
    --pace)
      PACE="${2:?--pace requires a value}"
      shift 2
      ;;
    --seconds)
      RECORD_SECONDS="${2:?--seconds requires a number}"
      shift 2
      ;;
    --output)
      OUTPUT_FILE="$2"
      shift 2
      ;;
    --help|-h)
      usage
      exit 0
      ;;
    *)
      echo "Unknown argument: $1" >&2
      usage >&2
      exit 2
      ;;
  esac
done

need_command() {
  if ! command -v "$1" >/dev/null 2>&1; then
    echo "Missing command: $1" >&2
    exit 1
  fi
}

need_command "$JAVA_BIN"
need_command "$JAVAC_BIN"
mkdir -p "$BIN_DIR" "$VIDEO_DIR"

echo "Compiling notepad and demo driver..."
"$JAVAC_BIN" -encoding UTF-8 -d "$BIN_DIR" "$SRC_DIR"/*.java "$SCRIPT_DIR/VideoDemoDriver.java"

case "$MODE" in
  dry-run)
    "$JAVA_BIN" -cp "$BIN_DIR" VideoDemoDriver --dry-run --pace "$PACE" --demo-file "$DEMO_FILE"
    exit 0
    ;;
  drive-only)
    "$JAVA_BIN" -cp "$BIN_DIR" VideoDemoDriver --pace "$PACE" --demo-file "$DEMO_FILE"
    exit 0
    ;;
esac

need_command screencapture
need_command swift

find_notepad_window_id() {
  swift - <<'SWIFT'
import CoreGraphics
import Darwin

let options = CGWindowListOption(arrayLiteral: .optionOnScreenOnly, .excludeDesktopElements)
guard let windows = CGWindowListCopyWindowInfo(options, kCGNullWindowID) as? [[String: Any]] else {
    exit(1)
}

var titleFallback: Int?

for window in windows {
    let layer = window[kCGWindowLayer as String] as? Int ?? -1
    if layer != 0 {
        continue
    }

    let owner = window[kCGWindowOwnerName as String] as? String ?? ""
    let title = window[kCGWindowName as String] as? String ?? ""
    let windowId = window[kCGWindowNumber as String] as? Int ?? 0
    let bounds = window[kCGWindowBounds as String] as? [String: Any] ?? [:]
    let width = bounds["Width"] as? Int ?? 0
    let height = bounds["Height"] as? Int ?? 0

    if windowId <= 0 || width < 500 || height < 350 {
        continue
    }

    let ownerMatches = owner == "NotepadApp" || owner.localizedCaseInsensitiveContains("java")
    let titleMatches = title.contains("AI 记事本")
        || title.contains("demo-note.txt")
        || title.contains("无标题")

    if ownerMatches && titleMatches {
        print(windowId)
        exit(0)
    }

    if titleFallback == nil && titleMatches {
        titleFallback = windowId
    }
}

if let fallback = titleFallback {
    print(fallback)
    exit(0)
}

exit(1)
SWIFT
}

wait_for_notepad_window_id() {
  local timeout_seconds="$1"
  local deadline=$((SECONDS + timeout_seconds))
  local window_id=""

  while (( SECONDS < deadline )); do
    if window_id="$(find_notepad_window_id 2>/dev/null)" && [[ -n "$window_id" ]]; then
      echo "$window_id"
      return 0
    fi
    sleep 0.5
  done

  echo "Could not find the AI notepad window to record." >&2
  echo "If the window is visible, grant Accessibility/Screen Recording permission to the current terminal or Codex." >&2
  return 1
}

if [[ -z "$RECORD_SECONDS" ]]; then
  RECORD_SECONDS="$("$JAVA_BIN" -cp "$BIN_DIR" VideoDemoDriver --duration-seconds --pace "$PACE" --demo-file "$DEMO_FILE" --recording-lead-in-ms "$WINDOW_RECORDING_LEAD_IN_MS")"
fi

rm -f "$OUTPUT_FILE"
echo "Recording to: $OUTPUT_FILE"
echo "Pace: $PACE"
echo "Record length: ${RECORD_SECONDS}s"
echo "Recording target: AI notepad window only"

DRIVER_PID=""
CAPTURE_PID=""
CLEANUP_DONE=0

cleanup_recording() {
  if [[ "$CLEANUP_DONE" == "1" ]]; then
    return
  fi
  if [[ -n "$CAPTURE_PID" ]] && kill -0 "$CAPTURE_PID" >/dev/null 2>&1; then
    kill "$CAPTURE_PID" >/dev/null 2>&1 || true
  fi
  if [[ -n "$DRIVER_PID" ]] && kill -0 "$DRIVER_PID" >/dev/null 2>&1; then
    kill "$DRIVER_PID" >/dev/null 2>&1 || true
    wait "$DRIVER_PID" >/dev/null 2>&1 || true
  fi
}

trap cleanup_recording EXIT INT TERM

"$JAVA_BIN" -cp "$BIN_DIR" VideoDemoDriver --pace "$PACE" --demo-file "$DEMO_FILE" --recording-lead-in-ms "$WINDOW_RECORDING_LEAD_IN_MS" --keep-open-after-demo &
DRIVER_PID=$!

WINDOW_ID="$(wait_for_notepad_window_id "$WINDOW_ID_TIMEOUT_SECONDS")"
echo "Window ID: $WINDOW_ID"
echo "Starting window recording..."

screencapture -v -l "$WINDOW_ID" -V"$RECORD_SECONDS" "$OUTPUT_FILE" &
CAPTURE_PID=$!

echo "Waiting for screen recording to finish..."
set +e
wait "$CAPTURE_PID"
CAPTURE_STATUS=$?
set -e
CAPTURE_PID=""

if [[ "$CAPTURE_STATUS" -ne 0 ]]; then
  echo "Recording failed with exit code $CAPTURE_STATUS: $OUTPUT_FILE" >&2
  echo "Check macOS System Settings > Privacy & Security > Screen Recording." >&2
  exit "$CAPTURE_STATUS"
fi

if [[ -n "$DRIVER_PID" ]] && kill -0 "$DRIVER_PID" >/dev/null 2>&1; then
  kill "$DRIVER_PID" >/dev/null 2>&1 || true
  wait "$DRIVER_PID" >/dev/null 2>&1 || true
fi
DRIVER_PID=""
CLEANUP_DONE=1
trap - EXIT INT TERM

if [[ ! -s "$OUTPUT_FILE" ]]; then
  echo "Recording failed or output file is empty: $OUTPUT_FILE" >&2
  echo "Check macOS System Settings > Privacy & Security > Screen Recording." >&2
  exit 1
fi

echo "Done: $OUTPUT_FILE"
