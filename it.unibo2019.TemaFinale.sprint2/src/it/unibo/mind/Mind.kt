/* Generated by AN DISI Unibo */ 
package it.unibo.mind

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Mind ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var forward = false
			  var obstacle = false
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("###MIND STARTED ")
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
					}
					 transition(edgeName="t04",targetState="handleAction",cond=whenEvent("modelChangedAction"))
					transition(edgeName="t05",targetState="handleSonar",cond=whenEvent("sonarRobot"))
					transition(edgeName="t06",targetState="reply",cond=whenDispatch("isObstacle"))
				}	 
				state("reply") { //this:State
					action { //it:State
						if(obstacle){ forward("obstacle", "obstacle" ,"roomexploration" ) 
						 }
						else
						 { forward("notObstacle", "notObstacle" ,"roomexploration" ) 
						  }
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("handleAction") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("modelChangedAction(DEST,ACTION)"), Term.createTerm("modelChangedAction(robot,ACTION)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								forward("robotAction", "robotAction(${payloadArg(1)})" ,"butler" ) 
								forward = (payloadArg(1) == "w")
								if(payloadArg(1).equals("a")){ delay(160) 
								forward("modelChangeAction", "modelChangeAction(robot,h)" ,"butlerresourcemodel" ) 
								 }
								if(payloadArg(1).equals("d")){ delay(180) 
								forward("modelChangeAction", "modelChangeAction(robot,h)" ,"butlerresourcemodel" ) 
								 }
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("handleSonar") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("sonarRobot(DISTANCE)"), Term.createTerm("sonarRobot(Distance)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								val Distance = Integer.parseInt(payloadArg(0));
								if(Distance <= 10 && forward){ println("###Mind, Obstacle at $Distance")
								forward("modelChangeAction", "modelChangeAction(robot,h)" ,"butlerresourcemodel" ) 
								emit("obstacleDetected", "obstacleDetected" ) 
								 }
								if(Distance <= 22){ println("###MIND PER EXPLORATION OBSTACLE")
								obstacle = true
								 }
								else
								 { obstacle = false
								  }
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}
