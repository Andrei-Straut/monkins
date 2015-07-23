# Monkins (Multiple Jenkins Monitor)

### What is it?
Monkins is a web app that allows you to monitor the state of jobs aggregated from various separate [Jenkins](https://jenkins-ci.org/)  servers / instances. No need for installing plugins or any extra work on the Jenkins side, just plugin the urls of the jobs you want to track and Monkins does the rest

### Why does it exist?
It exists because many team nowadays have dependencies on external modules / plugins, over which they have no control. If some of those dependencies are compiled or built in an accessible Jenkins, now you can monitor their state in order to get a view of their state, and subsequently, your project

### What do I need in order to install and run it?
Apart from the regular things you use to deploy and run web apps, 1 thing: your web server must support [WebSockets](https://en.wikipedia.org/wiki/WebSocket).

### What can it do?
It can connect to several Jenkins servers and get statuses of the defined job in (pretty much) real time, and display them in a pretty radiator-like interface.
There's also a pretty easy-to-use interface to define general settings and jobs. It's located at
```java
    <monkins_url>/settings
```

### What can't it do?
At the moment, we don't handle Jenkins API authentication. If the Jenkins server you like to check is behind a password / API token, we can't connect to it. However, implementing this should be easy, and it's on the roadmap.

Also, at the moment we don't handle Monkins authentication (and by this I mean authentication for settings edit). Also, implementing this should also be relatively easy, and if needed, just ask and I'll do it.

WebSockets might also be a limitation in some environments. In order to avoid constant polling and refreshes from the browser page, Monkins instead polls from the server and pushes only the updates to the client. This means that both the server where Monkins runs and the browser used to access it must support WebSockets (no worries, everything newer than Internet Explorer 9, and Chrome of Firefox newer than ~3 years already do). A list of who does it can be found [here](http://caniuse.com/#feat=websockets).

### How do I set it up?
Just deploy Monkins on your server, than head up to
```java
    <deploy_url>/settings
```
and put in the urls you want to monitor. Everything is pretty much explained there, and I did my best to make it easy.

### Do you take contributions / How can I contribute?
Sure I do. Just head over to the issues page, pick one, clone the repository, fix the issue, and submit a pull request. I promise I'll review and integrate it fast

### What are the plans for future development?
At the moment, the only things I think are missing are Jenkins authentication and Monkins settings page authentication. Once those are set, nothing much else, however, I'm open to suggestions.

### Special thanks
[Bootstrap](http://getbootstrap.com/) - no explanation needed, it just works!

[AngularJS](https://angularjs.org/) was love at first sight, and a lasting relationship since then, 'nuff said

[Angular UI notification](https://github.com/alexcrack/angular-ui-notification) because users should always be in the loop