# Scala With Cats

I am writing notes to myself when I am reading book called [scala with cats](https://underscore.io/books/scala-with-cats/). 
cat word is too much over loaded so I have changed model data from `Cat` to `Person` 
 

### Chapter 1 (Introduction)

 - Type classes
 - Cats Show
 - Cats Eq
 - Variance
    - Covariance`trait F[+A]` : F[B] is a subtype of F[A] if B is subtype of A 
    (B is a subtype of A, if we can use value of B anywhere we use the value of A )
    - Contravariance`trait F[-A]` : F[B] is a subtype of F[A] if A is subtype of B
    - Novariance`trait F[A]` : F[B] and F[A] are not subtypes what ever the relationship between A and B
 
### Chapter 2 (Monoids & Semigroups)

  `Monoid` has a associate binary addition (or combine) and an identity
element. 

  `Semigroup` has only associative binary addition. 
  
  Simple definition:
  
  ```
  trait Semigroup[A] {
    def combine(a: A, b: A): A
  }
  
  trait Monoid[A] extends Semigroup[A] {
    def empty: A
  }
  ```
  
  For example positive integers are `semigroup` not `monoid`. With zero element it 
is a `monoid`


### Chapter 3 (Functors)

  `Functor` is a type `F[A]` with an operation map with type `(A => B) => F[B]`
  
  `Functor` laws are 

  - __Identity__ : calling map with identity function is same as doing nothing
  - __Composition__ : mapping with two functions f and g is the same as 
  mapping with f and then mapping with g
  
  ```
     trait Functor[F[_]] {
       def map[A, B](fa: F[A])(f: A => B): F[B]
     }
     
     trait Contravariant[F[_]] {
       def contramap[A, B](fa: F[A])(f: B => A): F[B]
     }
     
     trait Invariant[F[_]] {
       def imap[A, B](fa: F[A])(f: A => B)(g: B => A): F[B]
     }

  ```
  
  
### Chapter 4 (Monads)

  A monad is a mechanism for sequencing computations. A simple monadic definition is 

```
trait Monad[F[_]] {
  def pure[A](value: A): F[A]
  def flatMap[A, B](value: F[A])(func: A => F[B]): F[B]
}
```

  A monad has 3 laws
  
  - __Left Identity__ : calling pure and transforming the result with func is the same as calling func
  
  `pure(a).flatMap(func) == func(a)`
  
  - __Right Identity__: passing pure to flatMap is the same as doing nothing
  
  `m.flatMap(pure) == m`
  
  - __Associativity__ : flatMapping over two func ons f and g is the same as flatMapping over f and then flatMapping over g
  
  `m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))`
  
### Resources 

- [Type Constructors, Functors, and Kind Projector](https://www.youtube.com/watch?v=Dsd4pc99FSY)
- [Higher-kinded types](https://typelevel.org/blog/2016/08/21/hkts-moving-forward.html)
- [Macros](https://docs.scala-lang.org/overviews/macros/overview.html)
- [Reflection Overview](https://docs.scala-lang.org/overviews/reflection/overview.html)