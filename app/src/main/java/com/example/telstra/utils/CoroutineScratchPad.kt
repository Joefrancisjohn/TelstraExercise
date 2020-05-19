package com.example.telstra.utils


import kotlinx.coroutines.*
import kotlinx.coroutines.channels.produce
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread
/*

    fun main(args: Array<String>) = runBlocking{

    launchChildrenSupervisor()
    try {
       // println("launchChildren result = ${launchChildren()}")
        launchChildrenSupervisor()
      //  deadKids()
    } catch (e: Exception) {
        println("Parent made to catch Exception")
    }
  //  enthoKoppu()

    //exampleAsyncAwait()
    //exampleWithContext()
    /// exampleBlockingDispatcher()
*/

/*    GlobalScope.launch(Dispatchers.IO){
        delay(2000)
        println("Delay in main - OVER ${Thread.currentThread().name}")
    }*//*


    //Thread.sleep(3000)


    println("fun main OVER Thread : ${Thread.currentThread().name}")
}

*/


fun enthoKoppu() = runBlocking<Unit> {
    val job = launch {
        repeat(10) {
            println("enthoKoppu")
            delay(500)
        }
    }

    delay(2000) // delays for launched coroutine to run for a while
    println("cancelling enthoKoppu")
    job.cancel()
}
suspend fun printlnDelayed(message: String) {
    // Complex calculation
    delay(1000)
    println(message+" ${Thread.currentThread().name}")
}


fun exampleBlocking() = runBlocking{
    println("one ${Thread.currentThread().name}")
    val job = launch(Dispatchers.IO) {
        printlnDelayed("two")
    }
    println("three ${Thread.currentThread().name}")
}

// Running on another thread but still blocking the main thread
fun exampleBlockingDispatcher(){
    runBlocking(Dispatchers.Default) {
        println("one - from thread ${Thread.currentThread().name}")
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    // Outside of runBlocking to show that it's running in the blocked main thread
    println("three - from thread ${Thread.currentThread().name}")
    // It still runs only after the runBlocking is fully executed.
}


fun exampleLaunchGlobalWaiting() = runBlocking {
    println("one - from thread ${Thread.currentThread().name}")

    val job = GlobalScope.launch {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }

    println("three - from thread ${Thread.currentThread().name}")
    job.join()
}
suspend fun calculateHardThings(startNum: Int): Int {
    delay(2000)
    println("calculateHardThings startNum = $startNum  ${Thread.currentThread().name}")
    if(startNum ==40)
        throw Exception("Hi There!")
    return startNum * 10
}


fun exampleAsyncAwait() = runBlocking {

    println("exampleAsyncAwait ${Thread.currentThread().name}")
    val startTime = System.currentTimeMillis()

    val deferred1 = async(Dispatchers.IO) { calculateHardThings(10) }
    val deferred2 = async { calculateHardThings(20) }
    val deferred3 = async {

        try {
            calculateHardThings(40)
        } catch (e: Exception) {
            println(e.toString())
            0
        }
    }

    val sum = deferred1.await() + deferred2.await() + deferred3.await()
    println("async/await result = $sum")

    val endTime = System.currentTimeMillis()
    println("Time taken: ${endTime - startTime}")
}

fun exampleWithContext() = runBlocking {
    val startTime = System.currentTimeMillis()
    val result1 = withContext(Dispatchers.Default) { calculateHardThings(10) }
    val result2 = withContext(Dispatchers.Default) { calculateHardThings(20) }
    val result3 = withContext(Dispatchers.Default) { calculateHardThings(30) }

    val sum = result1 + result2 + result3
    println("async/await result = $sum")

    val endTime = System.currentTimeMillis()
    println("Time taken: ${endTime - startTime}")
}


fun deadKids() = runBlocking<Unit>{
    launch(Dispatchers.Default) {
        repeat(5) {
            println("coroutine deadKids 1: $it")
            delay(100)
        }
    }

    launch(Dispatchers.Default) {
        repeat(5) {
            println("coroutine deadKids 2: $it")
            delay(150)
            if(it==1)
                throw Exception("Die BH!")
        }
    }

    delay(1500)
    println("cancelling deadKids")
    coroutineContext[Job]?.cancel()
}

suspend fun launchChildren(): Int = coroutineScope {
    try {
        val first = async { firstValue() }

        val second = async { secondValue() }
        first.await() + second.await()
    } catch (e: Exception) {
        println("Exception Caught in launchChildren")
        4
    }
}

suspend fun firstValue(): Int {
    delay(300)
    return 10
}

suspend fun secondValue(): Int {
    delay(250)
    //return 20
    throw(ArithmeticException("error occurred"))
}

suspend fun launchChildrenSupervisor() = supervisorScope   {
    println("launchChildrenSupervisor() START")
    launch(Dispatchers.Default) {
        repeat(5) {
            println("coroutine 1: count $it")
            if(it == 2) {
                throw (ArithmeticException("not very nice error"))
            }
            delay(500)
        }
    }

    launch(Dispatchers.Default) {
        repeat(5) {
            println("coroutine 2: count $it")
            delay(500)
        }
    }
}

fun main() = runBlocking {
    val producer = produce<Int>(capacity = 2) {
        (0..10).forEach {
            send(it)
            println("sent $it")
        }
    }
}