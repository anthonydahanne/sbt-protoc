#!/usr/bin/env bash
set -e

if [ "$(uname)" == "Darwin" ]; then
    brew update
    brew install bdw-gc
    brew link bdw-gc
    brew install jq
    brew install re2
    brew install llvm
    export PATH="/usr/local/opt/llvm@9/bin:$PATH"
fi

if [ "$(uname)" == "Linux" ]; then
    # Use pre-bundled clang
    export PATH=/usr/local/clang-5.0.0/bin:$PATH
    export CXX=clang++

    sudo apt-get install libgc-dev libunwind8-dev libre2-dev
fi
