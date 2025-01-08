
## Compilation with GitHub Action

1. Create your own `release.keystore` to replace `TMessagesProj/release.keystore`.

2. Prepare LOCAL_PROPERTIES

- KEYSTORE_PASS: from your keystore
- ALIAS_NAME: from your keystore
- ALIAS_PASS: from your keystore
- TELEGRAM_APP_ID: from [Telegram Developer](https://my.telegram.org/auth)
- TELEGRAM_APP_HASH: from [Telegram Developer](https://my.telegram.org/auth)

```env
KEYSTORE_PASS=123456
ALIAS_NAME=key0
ALIAS_PASS=123456
TELEGRAM_APP_ID=123456
TELEGRAM_APP_HASH=abcdefg
```

Then, use base64 to encode the above.

3. Add Repo Action Secrets

- LOCAL_PROPERTIES: from step 2

4. Run Release Build

## FAQ

#### What is the noGcm version?

Google Cloud Messaging, also known as gcm / fcm, message push service by google used by original Telegram android app, it requires your device to have Google Service Framework (non-free) installed.

#### I've encountered a bug!

First, make sure you have the latest version installed (check the channel).

Then, if the issue appears in the official Telegram client too, please submit it to the officials, (be careful not to show NekoX in the description and screenshots, the official developers doesn't like us!).

Then, submit it to our [group](https://t.me/nagram_group) with #bug.

If you experience a *crash*, you also need to click on the version number at the bottom of the settings and select "Enable Log" and send it to us.

## Localization

Nagram is forked from Telegram, thus most locales follows the translations of Telegram for Android, checkout <https://translations.telegram.org/en/android/>.

Is Nagram not in your language, or the translation is incorrect or incomplete? Get involved in the translations on our [Weblate](https://xtaolabs.crowdin.com/nagram).

[![Crowdin](https://badges.crowdin.net/e/156df3a631d257cc6b57301566d545fb/localized.svg)](https://xtaolabs.crowdin.com/nagram)

## Thanks

- [NekoX](https://github.com/NekoX-Dev/NekoX)
- [Nekogram](https://gitlab.com/Nekogram/Nekogram)
- [Pigeongram](https://gitlab.com/JasonKhew96/Nekogram)
- [Nullgram](https://github.com/qwq233/Nullgram)
- [TeleTux](https://github.com/TeleTux/TeleTux)
- [OwlGram](https://github.com/OwlGramDev/OwlGram)
