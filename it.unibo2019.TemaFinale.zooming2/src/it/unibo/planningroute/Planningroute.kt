/* Generated by AN DISI Unibo */ 
package it.unibo.planningroute

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Planningroute ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("###PLANNINGROUTE STARTED")
					}
					 transition( edgeName="goto",targetState="waitGoal", cond=doswitch() )
				}	 
				state("waitGoal") { //this:State
					action { //it:State
					}
					 transition(edgeName="t013",targetState="planningRoute",cond=whenDispatch("calculateRoute"))
				}	 
				state("planningRoute") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("calculateRoute(X)"), Term.createTerm("calculateRoute(GOAL)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("###PLANNINGROUTE calculate route to ${payloadArg(0)}")
								forward("exec", "exec" ,"execroute" ) 
						}
					}
					 transition( edgeName="goto",targetState="waitGoal", cond=doswitch() )
				}	 
			}
		}
}
