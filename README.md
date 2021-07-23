# nanolens

[![CI](https://github.com/aoiroaoino/nanolens/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/aoiroaoino/nanolens/actions/workflows/ci.yml)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dev.aoiroaoino/nanolens/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dev.aoiroaoino/nanolens)

Nanolens is tiny and experimental lens library for Scala 3

```scala
type Lens[S, A] = [F[_]] => Functor[F] ?=> (A => F[A]) => S => F[S]

type Prism[S, T, A, B] = [P[_, _], F[_]] => (Choice[P], Applicative[F]) ?=>
  P[A, F[B]] => P[S, F[T]]
```

### Installation

```sbt
libraryDependencies += "dev.aoiroaoino" %% "nanolens" % "[version]"
```

### Example

```scala
import dev.aoiroaoino.nanolens._

case class User(id: Int, name: String, address: Address)
case class Address(street: String)

def address: Lens[User, Address] =
  Lens[User, Address](_.address)(u => a => u.copy(address = a))

def street: Lens[Address, String] =
  Lens[Address, String](_.street)(a => s => a.copy(street = s))
  
// ===
  
val user = User(42, "John", Address("street"))

user $ (address ~ street).modify(_.toUpperCase)
// => User(42,John,Address(STREET))

user $ (address ~ street).get
// => street
```
