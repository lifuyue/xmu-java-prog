#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
LAB_DIR="$ROOT_DIR/LAB7"

usage() {
    echo "用法: bash LAB7/run.sh <题号>"
    echo "可用题号: 1 2 3 4 5 6 7"
    echo "示例: bash LAB7/run.sh 1"
    echo "需要输入的小题可以使用: printf \"54\\n\" | bash LAB7/run.sh 7"
}

if [[ $# -lt 1 ]]; then
    usage
    exit 1
fi

case "$1" in
    1) QUESTION="1"; MAIN_CLASS="GenericEqualityDemo" ;;
    2) QUESTION="2"; MAIN_CLASS="PairDemo" ;;
    3) QUESTION="3"; MAIN_CLASS="CarbonFootprintDemo" ;;
    4) QUESTION="4"; MAIN_CLASS="UniqueNames" ;;
    5) QUESTION="5"; MAIN_CLASS="RepeatedWordCounter" ;;
    6) QUESTION="6"; MAIN_CLASS="LetterCount" ;;
    7) QUESTION="7"; MAIN_CLASS="PrimeFactorsWithSet" ;;
    *)
        usage
        exit 1
        ;;
esac

SRC_DIR="$LAB_DIR/$QUESTION/src"
BIN_DIR="$LAB_DIR/$QUESTION/bin"

mkdir -p "$BIN_DIR"
javac -d "$BIN_DIR" "$SRC_DIR"/*.java
java -cp "$BIN_DIR" "$MAIN_CLASS"
