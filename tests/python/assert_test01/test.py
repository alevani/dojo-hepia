from assertpy import assert_that
import main as m

assert_that(m.easyline([1,2])).is_equal_to([2,4])
assert_that(m.easyline([45,53,12])).is_equal_to([90,106,24])
assert_that(m.easyline([3])).is_equal_to([6])