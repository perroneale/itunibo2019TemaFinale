/* Generated by AN DISI Unibo */ 
package it.unibo.ctxFridge
import it.unibo.kactor.QakContext
import it.unibo.kactor.sysUtil
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
	QakContext.createContexts(
	        "192.168.43.100", this, "sprint4.pl", "sysRules.pl"
	)
}

