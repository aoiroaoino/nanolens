package dev.aoiroaoino.nanolens

object Example {

  case class User(id: Int, name: String, address: Address)
  case class Address(street: String)

  def address: Lens[User, Address] =
    Lens[User, Address](_.address)(u => a => u.copy(address = a))

  def street: Lens[Address, String] =
    Lens[Address, String](_.street)(a => s => a.copy(street = s))

//  def addressStreet: Lens[User, String] =
//    [F[_]] => (F: Functor[F]) ?=>
//      address[F] compose street[F]

  def main(args: Array[String]): Unit = {
    val user = User(42, "John", Address("street"))
    println { user $ (address ~ street).modify(_.toUpperCase) }
    println { user $ (address ~ street).get }

    println { user $ right.review }
    println { Either.cond(false, 42, "error") $ left.preview }
  }
}
