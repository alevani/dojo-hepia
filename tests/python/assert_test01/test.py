def mult(arr,n):
    return [i*n for i in arr]

from assertpy import assert_that
import sample as m

assert_that(m.mult([2,5],2)).is_equal_to([4,10])
