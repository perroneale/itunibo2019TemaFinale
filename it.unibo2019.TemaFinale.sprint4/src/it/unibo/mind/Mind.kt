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
			  var rotation = false
			  var rotatory = 0
			  var NumRot = 0
			  var TimeVirtual = 0L
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("###MIND STARTED ")
						solve("consult('config.pl')","") //set resVar	
						solve("rotation(X)","") //set resVar	
						if(currentSolution.isSuccess()) { NumRot = Integer.parseInt(getCurSol("X").toString())
						 }
						solve("timeVirtual(TV)","") //set resVar	
						if(currentSolution.isSuccess()) { TimeVirtual = getCurSol("TV").toString().toLong()
						println("$TimeVirtual")
						 }
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
					}
					 transition(edgeName="t04",targetState="handleAction",cond=whenDispatch("modelChangedAction"))
					transition(edgeName="t05",targetState="handleSonar",cond=whenEvent("sonarRobot"))
					transition(edgeName="t06",targetState="handleRotatory",cond=whenEvent("rotatoryCounter"))
					transition(edgeName="t07",targetState="reply",cond=whenDispatch("isObstacle"))
				}	 
				state("handleRotatory") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("rotatoryCounter(COUNTER)"), Term.createTerm("rotatoryCounter(COUNTER)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("*******COUNTER ${payloadArg(0)}**********")
								rotatory = Integer.parseInt(payloadArg(0))
								if((rotatory < 0)){ rotatory = -rotatory
								 }
								if((rotation && rotatory >= NumRot )){ println("&&&&&&STOP FOR ROTATORY ENCODER")
								forward("robotAction", "robotAction(h)" ,"butler" ) 
								forward("modelUpdateAction", "modelUpdateAction(robot,h)" ,"butlerresourcemodel" ) 
								 }
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
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
											  rotation = ((payloadArg(1) == "d") || (payloadArg(1) == "a"))
								println("$rotation")
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("sendStop") { //this:State
					action { //it:State
						println("SEND STOP")
						forward("robotAction", "robotAction(h)" ,"butler" ) 
						forward("modelUpdateAction", "modelUpdateAction(robot,h)" ,"butlerresourcemodel" ) 
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("handleSonar") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("sonarRobot(DISTANCE)"), Term.createTerm("sonarRobot(Distance)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								val Distance = Integer.parseInt(payloadArg(0));
								if(Distance <= 10 && forward){ println("###Mind, Obstacle at $Distance")
								forward("robotAction", "robotAction(h)" ,"butler" ) 
								forward("modelUpdateAction", "modelUpdateAction(robot,h)" ,"butlerresourcemodel" ) 
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
