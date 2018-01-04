# Scala With Cats

I am writing notes to myself when I am reading book called [scala with cats](https://underscore.io/books/scala-with-cats/). 
cat word is too much over loaded so I have changed model data from `Cat` to `Person` 
 

# Chapter 1 (Introduction)

 - Type classes
 - Cats Show
 - Cats Eq
 - Variance
 
# Chapter 2 (Monoids & Semigroups)

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
