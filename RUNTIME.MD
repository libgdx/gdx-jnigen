### What is it?
The jnigen-runtime allows for easy interoperation between java and C code.  
It supports Structs, Unions, Enums, Pointer and callbacks.  

The jnigen-runtime technically consists of two parts:
- The generator, used to generate bindings from a C header
- The runtime, used to actually handle the C <-> Java interop

### The Generator
The generator is a libclang based parser, to parse C header files.  
The generator can be configured through the gradle plugin.  
```groovy
    generator {
        outputPath = file("dir/to/java/source")
        basePackage = "my.package.name"
        fileToParse = "cHeader.h" // This is not a path. It is used as an include statement like `#include "cHeader.h"` 
        options = arrayOf(...) // Here you can set the include path and all the other flags that should be passed to clang
    }
```
The generator can then be run through `./gradlew jnigenGenerateBindings`

The generator will then emit the java code. The main class will be named after your header file and will contain all the main C entry points.   
In the `enums` and `structs` sub-package will be Enums/Structs/Unions put.  
In the `Constants` class will be potential useful constants. All other classes are implementation detail.

The generated code is intended to be consumed by jnigen again.  
The intended workflow is, to statically link the lib you are trying to bind into the jnigen build. Dynamic linking is only advised if really necessary.  

### The Runtime
The runtime describes the interop library and the generated code.
Not all classes should be considered public API. Of interest are mainly the classes:
- All inside `com.badlogic.gdx.jnigen.runtime.pointer`
- The classes `com.badlogic.gdx.jnigen.runtime.c.CEnum/CXXException`
- The classes `com.badlogic.gdx.jnigen.runtime.closure.Closure/ClosureObject/PointingPoolManager`

Everything else is rather not public API.

On startup a few configuration can be done over properties:
- `com.badlogic.jnigen.allocator.no_pooling`: Whether some internal data structures should be subject to pooling (Default: true)
- `com.badlogic.jnigen.allocator.pool_size`: How large the pool for internal data structures should be (Default: 256)

Many allocations on `BufferPtr` objects could indicate a too small pool size.

#### Inability to map c types to java types
C has lots of int types, which don't have a direct java equivalent. Even more, some sizes are platform dependent.  
The generated bindings will ensure, that the passed values to C are of the correct size on the platform.  
Here is a short overview on problematic types and how they map to java:
- char -> byte: Signess is platform dependent.
- long -> long: On 32bit systems and windows 4 bytes, while 8 bytes on everything else
- unsigned char -> char: char in java can represent 2 bytes, while char in C only 1 byte.
- unsigned int -> long: long in java can represent 8 bytes, while int in C only 4 byte.
- unsigned long -> long: long in java can't be represented unsigned
- unsigned long long -> long: long in java can't be represented unsigned

#### Calling C functions
In your main class bindings to all C functions are generated. They will have the same name as the C function.  
There will always be a function, that has "_internal" appended to the name. This function is not intended for direct use.  
If the function only returns primitive values/void, there is not much to consider.  
If the function returns a pointer/struct/union, every function will have an overload, that allows you to pass a pre-allocated object.  
These functions will override the content of what you are passing, so use with caution. Only use them, if you have to many object allocations otherwise.

#### Pointer in general
The class `Pointing` is the base class of everything that holds an address.  
The complicated part of C <-> Java interop is, that java code has a garbage collector, while C is fully manually managed.  
Because of that, every `Pointing` can exist in two modes: GC managed or manual managed.  
Pointer allocated in java are by default GC managed. The memory will be freed, once the GC claims the pointing.  
However, this can be always configured over the constructor.  
When allocating a java `Pointing` descendant, the question always needs to be, who and when is the memory supposed to be released.  
There are multiple unsafe methods exposed in `Pointing`. Always check javadocs before use.

#### Structs/Unions
Structs/Unions, also referred as `StackElement`s model pure structs in C.  
They are expected to be only really allocated in java, as they also only live on the stack in "C".  
They are passed-by-value, so even tho the code might pass a "long" pointer to native code, changes to the passed struct won't be reflected in java and vice versa.  
Incidentally, a StackElement also doesn't have a real identity. The identity of a StackElement is defined by its value(s).  
A StackElement can be re-interpreted as a Pointer via the `asPointer` method. Passing such a pointer to C will result in modification being reflected in java and the other way around. It is the java equivalent of the ampersand operator in C.    

A StackElement might contain inner structs or unions. There will be a get/set method, to clone/write to that inner StackElement directly. There will be also a method named like the field. The returned StackElement object will allow to write/read from the inner StackElement directly. Changes to the returned StackElement will be directly reflected to the outer StackElement.

The `StackElementPointer` works similar to the other pointer. The major difference is the existence of `asStackElement`.  
This method is the counter-part of `asPointer`. It re-interprets an address as a StackElement. This means, modification to the StackElement will be reflected in the pointer and vice versa.

#### Enums
A C enum is by default mapped to a java enum. However, enums in C are more flexible, as they are not compile-time fixed like in java.  
If this is an issue, please report back.  
Everything else in the enum handling is straight forward.  

#### Closure
Closures are implemented as interfaces in java. They can be allocated with `ClosureObject#fromClosure`.  
They have a manual lifecycle and need to be freed manually.  
When retrieving a java closure from C again, the object will be identical.  
C function pointer are also wrapped in a java interface. A downcall can be done over `ClosureObject#getClosure`.  

Exceptions thrown in a java closure are passed through the call-stack, back to the last java frame. If no java frame exists, a C++ exception will be thrown.

#### Pointer
The Int/Enum/Float/StackElement/Double/PointerPointer pointer are rather straight forward. They have a get/set method, to get/set fields.

#### PointerPointer
PointerPointer is a bit special belong the Pointer types, as it maps cases like `void**`. The main difference is, to construct a PointerPointer, you need to supply a `PointerDereferenceSupplier`.  
This supplier will be used to construct the inner pointer type on dereference (as-in `get` method).

### Advanced Runtime
Here are some advanced consideration/options.

#### Closure pooling
When doing an upcall from C -> Java, the closure will allocate the relevant java object. This can be problematic if the upcall happens often, because it will accumulate lots of garbage.  
The way to avoid this is by setting a `PointingPoolManager` via `ClosureObject#setPoolManager`. This comes with many caveats, see `PointingPoolManager` javadoc for more information.  
The method only has an effect on C -> Java closures

#### Overload methods
Most methods that create objects via return have an overload, to pass a return object instead.  
On the passed object the pointer/data will be changed then, to what would be returned otherwise.  
The previous data/pointer will be lost. For pointer types, the pointer object is not allowed to be registered for GC.

#### Exceptions
C++ exceptions are caught and wrapped in the java `CXXException`, then rethrown.