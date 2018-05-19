1. To add this library to your app, initially download the ForceUpdate library to the root folder of your app.
2. Add the following line to your **settings.gradle** file <br>
**include ':app',':forceupdate'**<br>
3. Now go to app **build.gradle** and add the following line.<br>
**implementation project(':forceupdate')**<br>
4. Sync library and you're good to go.

Initiate **ForceUpate** class in your **MainActivity**

```
ForceUpdate forceUpdate = new ForceUpdate(MainActivity.this);
        forceUpdate.setCanceledOnTouchOutside(false);
        forceUpdate.setTitle("New Update Available");
        forceUpdate.setMessage("Download this Update for New Features");
        forceUpdate.build();
```


Available Methods in this ForceUpdate Class:

Upadte Dialog dismiss when user touches the ui other than dialog.

```setCanceledOnTouchOutside(boolean canceledOnTouchOutside)```

Dialog will dismiss only if user taps any one of the button.

```setCanceled(boolean canceled)```

Set custom title for the Update Dialog.

```setTitle(String title)```

Set Custom Message to show to the user.

```setMessage(String message)```



**Sample Screenshot:**

![AndroidAppForceUpdate](https://github.com/SamaGyani/ForceUpdateApp/blob/master/sample.png)

**Note:**
1. This library will work only when the application is connected to the internet.<br>
2. Internet permissions are already added to the library manifest file.<br>
