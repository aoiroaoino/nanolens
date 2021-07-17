package dev.aoiroaoino.nanolens

extension [A](a: A) {
  // e.g. s $ (foo ~ bar ~ baz).get
  def $[B](f: A => B): B = f(a)
}
