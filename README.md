## gdx-jnigen
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2Flibgdx%2Fgdx-jnigen&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=PAGE+VIEWS&edge_flat=false)](https://hits.seeyoufarm.com)

[![Build Status](https://libgdx.badlogicgames.com/jenkins/buildStatus/icon?job=gdx-jnigen)](https://libgdx.badlogicgames.com/jenkins/job/gdx-jnigen/)

The gdx-jnigen tool can be used with or without libgdx to allow C/C++ code to be written inline
with Java source code. 

This increases the locality of code that conceptually belongs together (the Java native class methods and the actual implementation) and makes refactoring a lot easier
compared to the usual JNI workflow. Arrays and direct buffers are converted for you, further
reducing boilerplate. Building the natives for Windows, Linux, OS X, and Android and iOS is handled for
you. jnigen also provides a mechanism for loading native libraries from a JAR at runtime, which
avoids "java.library.path" troubles.

See the LibGDX Wiki for usage: https://github.com/libgdx/libgdx/wiki/jnigen
