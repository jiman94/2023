# Back-end

```
res.header("Access-Control-Allow-Origin", "*");
res.header("Access-Control-Allow-Methods", "GET,PUT,PATCH,POST,DELETE");
res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
```

# Front-end

1안)

```
flutter run -d chrome --web-browser-flag "--disable-web-security"
```
2안) 
— disable-web-security

/Users/mz03-jmryu/Downloads/flutter/packages/flutter_tools/lib/src/web

```
      // Chrome launch.
      '--disable-extensions',
      '--disable-web-security',
      '--disable-popup-blocking',
      '--bwsi',
      '--no-first-run',
      '--no-default-browser-check',
      '--disable-default-apps',
      '--disable-translate',
``
