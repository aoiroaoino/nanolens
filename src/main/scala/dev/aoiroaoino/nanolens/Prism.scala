package dev.aoiroaoino.nanolens

type PPrism[S, T, A, B] = [P[_, _], F[_]] => (Choice[P], Applicative[F]) ?=>
  P[A, F[B]] => P[S, F[T]]

type Prism[S, A] = PPrism[S, S, A, A]

def PPrism[S, T, A, B]: (B => T) => (S => Either[T, A]) => PPrism[S, T, A, B] =
  bt => seta => [P[_, _], F[_]] => (P: Choice[P], F: Applicative[F]) ?=>
    (afb: P[A, F[B]]) => P.dimap(seta)((_: Either[T, F[B]]).fold(F.pure, F.map(_)(bt)))(P.right(afb))

def Prism[S, A]: (A => S) => (S => Either[S, A]) => Prism[S, A] =
  as => sesa => [P[_, _], F[_]] => (P: Choice[P], F: Applicative[F]) ?=>
    (afa: P[A, F[A]]) => P.dimap(sesa)((_: Either[S, F[A]]).fold(F.pure, F.map(_)(as)))(P.right(afa))

def left[L, R]: Prism[Either[L, R], L] =
  Prism[Either[L, R], L](Left(_)) {
    case Left(v) => Right(v)
    case r @ Right(_) => Left(r)
  }
def right[L, R]: Prism[Either[L, R], R] =
  Prism[Either[L, R], R](Right(_)) {
    case Right(v) => Right(v)
    case l @ Left(_) => Left(l)
  }

def some[A]: Prism[Option[A], A] =
  Prism[Option[A], A](Some(_)) {
    case Some(a) => Right(a)
    case None => Left(None)
  }
def none[A]: Prism[Option[A], Unit] =
  Prism[Option[A], Unit](_ => None) {
    case None => Right(())
    case s @ Some(_) => Left(s)
  }

extension [S, A](prism: Prism[S, A]) {

//  // TODO: Lens と同じ記号にしたい...
  def ~?[B](other: Prism[A, B]): Prism[S, B] =
    [P[_, _], F[_]] => (P: Choice[P], F: Applicative[F]) ?=>
      prism[P, F] compose other[P, F]

  def preview: S => Option[A] =
    s => prism[Function1, [X] =>> Const[First[A], X]](a => Const(First(a)))(s).getConst.getFirst

  def review: A => S =
    a => prism[Tagged, Id](Tagged(Id(a))).unTagged.runId
}
