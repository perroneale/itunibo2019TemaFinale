/* Generated by AN DISI Unibo */ 
package it.unibo.ctxFridge
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "localhost", this, "butlerzooming1_2.pl", "sysRules.pl"
	)
}

