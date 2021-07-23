package dev.aoiroaoino.nanolens

trait Functor[F[_]]:
  def map[A, B](fa: F[A])(f: A => B): F[B]

trait Applicative[F[_]] extends Functor[F]:
  def pure[A](a: A): F[A]

trait Profunctor[P[_, _]]:
  def dimap[A, B, C, D](ab: A => B)(cd: C => D)(pbc: P[B, C]): P[A, D]

trait Choice[P[_, _]] extends Profunctor[P]:
  def left[A, B, C](pab: P[A, B]): P[Either[A, C], Either[B, C]]
  def right[A, B, C](pab: P[A, B]): P[Either[C, A], Either[C, B]]

trait Monoid[A]:
  def empty: A
  def append(x: A, y: A): A

// ===

opaque type Id[A] = A
object Id {
  def apply[A](a: A): Id[A] = a
}
extension [A](id: Id[A]) {
  def runId: A = id
}

given Applicative[Id] with
  def map[A, B](fa: Id[A])(f: A => B): Id[B] = f(fa)
  def pure[A](a: A): Id[A] = a

opaque type Const[A, B] = A
object Const {
  def apply[A, B](a: A): Const[A, B] = a
}
extension [A, B](const: Const[A, B]) {
  def getConst: A = const
}

given [C](using Monoid[C]): Applicative[[X] =>> Const[C, X]] with
  def map[A, B](fa: Const[C, A])(f: A => B): Const[C, B] = fa
  def pure[A](a: A): Const[C, A] = summon[Monoid[C]].empty

opaque type Tagged[A, B] = B
object Tagged {
  def apply[A, B](b: B): Tagged[A, B] = b
}
extension [A, B](tagged: Tagged[A, B]) {
  def unTagged: B = tagged
}

given [C]: Applicative[[X] =>> Tagged[C, X]] with
  def map[A, B](fa: Tagged[C, A])(f: A => B): Tagged[C, B] = f(fa)
  def pure[A](a: A): Tagged[C, A] = a

given Choice[Tagged] with
  def dimap[A, B, C, D](ab: A => B)(cd: C => D)(pbc: Tagged[B, C]): Tagged[A, D] = cd(pbc)

  def left[A, B, C](pab: Tagged[A, B]): Tagged[Either[A, C], Either[B, C]] = Left(pab)
  def right[A, B, C](pab: Tagged[A, B]): Tagged[Either[C, A], Either[C, B]] = Right(pab)

opaque type First[A] = Option[A]
object First {
  def apply[A](a: A): First[A] = Some(a)
}
extension [A](first: First[A]) {
  def getFirst: Option[A] = first
}

given [A]: Monoid[First[A]] with
  def empty: First[A] = None
  def append(x: First[A], y: First[A]): First[A] = x orElse y

// ===

given Choice[Function1] with
  def dimap[A, B, C, D](ab: A => B)(cd: C => D)(pbc: B => C): A => D =
    cd compose pbc compose ab

  def left[A, B, C](pab: A => B): Either[A, C] => Either[B, C] = _.left.map(pab)
  def right[A, B, C](pab: A => B): Either[C, A] => Either[C, B] = _.map(pab)
