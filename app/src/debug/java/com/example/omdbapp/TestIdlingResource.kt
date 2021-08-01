package com.example.omdbapp

object TestIdlingResource {

    private const val RESOURCE = "GLOBAL"

    var countingIdlingResource = SimpleCountingIdlingResource(RESOURCE)


    fun increment(resourceName: String) {
        when (resourceName) {
            RESOURCE -> countingIdlingResource.increment()
        }

    }

    fun decrement(resourceName: String) {
        when (resourceName) {
            RESOURCE -> if (!countingIdlingResource.isIdleNow) {
                countingIdlingResource.decrement()
            }
        }

    }
}
