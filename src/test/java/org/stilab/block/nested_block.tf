locals {

    name = lookup([], null)

    filter {

        spec {
          a = 1
          b = 2
          c = 6
        }

        spec {
          a = 1
          b = 2
          c = 6
        }
    }

    dynamic {
      a = 1
      b = 2
      c = 6
      d = 8
      m = 5
    }

}
