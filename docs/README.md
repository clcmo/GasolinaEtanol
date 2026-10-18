# Gasolina-Etanol

App Android para comparar preços da Gasolina com o Etanol.

Reescrita do app de 2018 (Java, Android 8, support library) em Kotlin + Jetpack Compose. A versão Flutter será um projeto separado.

[![GitHub license](https://img.shields.io/github/license/clcmo/walking?style=for-the-badge)](https://github.com/clcmo/walking)
[![GitHub stars](https://img.shields.io/github/stars/clcmo/walking?style=for-the-badge)](https://github.com/clcmo/walking/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/clcmo/walking?style=for-the-badge)](https://github.com/clcmo/walking/network)
[![GitHub issues](https://img.shields.io/github/issues/clcmo/walking?style=for-the-badge)](https://github.com/clcmo/walking/issues)
[![GitHub donate](https://img.shields.io/github/sponsors/clcmo?color=pink&style=for-the-badge)](https://github.com/sponsors/clcmo)

[![CI](https://github.com/clcmo/walking/actions/workflows/ci.yml/badge.svg)](https://github.com/clcmo/walking/actions/workflows/ci.yml)
[![CD](https://github.com/clcmo/walking/actions/workflows/cd.yml/badge.svg)](https://github.com/clcmo/walking/actions/workflows/cd.yml)

Demo web: em breve.

## Estado atual (marco 2)

## Proximos marcos

## Como rodar (linha de comando, sem Android Studio)

Requisitos: JDK 17 ou superior, Android SDK (command-line tools) com `platform-tools`, `platforms;android-35` e `build-tools;35.0.0`, e `ANDROID_HOME` configurado.

```
./gradlew :app:testDebugUnitTest   # testes de domínio
./gradlew assembleDebug            # compila o APK
./gradlew installDebug             # instala no celular (depuração USB ligada)
adb logcat                         # logs
```

Para testar o GPS, use um celular de verdade e caminhe ao ar livre: o sinal em ambiente fechado é impreciso e os pontos serão descartados pelo filtro de 20 m de precisão.

## Versões

O catálogo (`gradle/libs.versions.toml`) usa um conjunto de versões estáveis e compatíveis entre si
(AGP 8.7, Kotlin 2.0, Compose BOM 2024.12). Para atualizar, altere os números do catálogo.
