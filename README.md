# CallEnder

========

An Android App trying to block unwanted call, and if not success,
at least turn to Do Not Disturb mode.

这个项目的起因是对抗中国烦人的骚扰电话。由于某些神奇的改号软件，
骚扰的号码往往改的很随意，或者可能前缀相同，而这使得对单个号码加入
黑名单来阻挡骚扰电话变得十分低效。因此我们迫切需要能够按模式匹配来
屏蔽骚扰电话的功能。然而，Android一方面没有提供有效的电话控制权限和
接口，另一方面Android 7.0后新加的电话黑名单功能也十分Naive。本项目只能
在尽可能的程度上，对骚然电话按模式匹配并自动断开电话或调整为静音模式。
