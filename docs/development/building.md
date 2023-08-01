# Building

## Setup

To build from source, checkout the repo using:

```shell
git clone --recurse-submodules https://github.com/DrewCarlson/KtSoup.git
```

Note that `lexbor` is a required submodule, if you do not checkout with `--recurse-submodules` you must run:

```shell
git submodule update --init --recursive
```

## Lexbor

For native targets, a static build of Lexbor is used.
Pre-compiled artifacts are available in Github at tags prefixed with `lexbor-vX.X.X`.

The current version is [`lexbor-v2.2.0`](https://github.com/DrewCarlson/KtSoup/releases/tag/lexbor-v2.2.0).

Normally when running the project locally, the required artifacts are downloaded automatically and placed into the
`lexbor-bin` folder at the root of the project.

If you would like to build it yourself, the `.github/workflows/build-lexbor.yml` workflow provides all the details to
make use of the `cmake-files` in this repository for cross-compilation.
When you've created your build, place it in the `lexbor-bin/<Kotlin Target>/` folder and build as usual.
