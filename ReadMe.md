# Scala With Cats

I am writing notes to myself when I am reading book called [scala with cats](https://underscore.io/books/scala-with-cats/). 
cat word is too much over loaded so I have changed model data from `Cat` to `Person` 
 

### Chapter 1 (Introduction)

 - Type classes
 - Cats Show
 - Cats Eq
 - Variance
 
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
  
### Resources 

- [Type Constructors, Functors, and Kind Projector](https://www.youtube.com/watch?v=Dsd4pc99FSY)
- [Higher-kinded types](https://typelevel.org/blog/2016/08/21/hkts-moving-forward.html)