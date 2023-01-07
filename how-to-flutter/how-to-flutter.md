# how-to-flutter

## Flutter & Dart

https://flutter.dev
https://dart.dev 
https://flutter.dev/learn


### Install Dart SDK


```shell
brew tap dart-lang/dart
brew install dart
```

### Install Flutter SDK

```shell
brew install flutter
```


```shell
flutter doctor
```

### iOS simulator


```
open -a Simulator
```

### iOS setup

```shell
softwareupdate --all --install --force
```

#### CocoaPods

```shell
sudo gem install cocoapods
```


### Android setup


```shell
brew install android-studio
```

```shell
open ~/.zshrc

which flutter 
```

```shell
flutter doctor
```

stless + tab 

## Start with Flutter development


```shell
flutter create how_to_flutter
```

## sttl + tab 

```dart
import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Welcome to Flutter',
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Welcome to Flutter'),
        ),
        body: const Center(
          child: Text('Hello World'),
        ),
      ),
    );
  }
}
```

```shell
flutter run
```

