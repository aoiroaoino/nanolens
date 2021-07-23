package dev.aoiroaoino.nanolens

type Lens[S, A] = [F[_]] => Functor[F] ?=>
  (A => F[A]) => S => F[S]

def Lens[S, A]: (S => A) => (S => A => S) => Lens[S, A] =
  sa => sas => [F[_]] => (F: Functor[F]) ?=>
    (afa: A => F[A]) => (s: S) => F.map(afa(sa(s)))(sas(s))

extension [S, A](lens: Lens[S, A]) {
  // compose メソッドで合成できるのは素敵だが、 毎回 `[F[_]] => (_: Functor[F]) ?=>` を書くのは面倒なので
  def ~[B](other: Lens[A, B]): Lens[S, B] =
    [F[_]] => (_: Functor[F]) ?=> lens[F] compose other[F]

  def get: S => A =
    s => lens[[X] =>> Const[A, X]](Const(_))(s).getConst

  def modify: (A => A) => S => S =
    f => s => lens[Id](a => Id(f(a)))(s).runId

  def set: A => S => S = a => modify(_ => a)
}
