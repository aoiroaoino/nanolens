package dev.aoiroaoino.nanolens

trait Functor[F[_]]:
  def map[A, B](fa: F[A])(f: A => B): F[B]

type Id[A] = A

given Functor[Id] with
  def map[A, B](fa: Id[A])(f: A => B): Id[B] = f(fa)

type Const[A, C] = C

given [C]: Functor[[X] =>> Const[X, C]] with
  def map[A, B](fa: Const[A, C])(f: A => B): Const[B, C] = fa

