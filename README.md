# nanolens

[![CI](https://github.com/aoiroaoino/nanolens/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/aoiroaoino/nanolens/actions/workflows/ci.yml)

Nanolens is tiny and experimental lens library for Scala 3

```scala
type Lens[S, A] = [F[_]] => Functor[F] ?=> (A => F[A]) => S => F[S]
```

### Installation

- WIP

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