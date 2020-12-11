/* Generated by AN DISI Unibo */ 
package it.unibo.onestep

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Onestep ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		 
				var foundObstacle = false; 
				var StepTime = 0L; 
				var Duration=0L 
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("onestepahead STARTED")
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
						foundObstacle = false 
						println("IN WAITCMD")
					}
					 transition(edgeName="t00",targetState="doMoveForward",cond=whenDispatch("onestep"))
				}	 
				state("doMoveForward") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("onestep(DURATION)"), Term.createTerm("onestep(TIME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								StepTime = payloadArg(0).toLong()
								println("STEP TIME $StepTime")
								forward("modelChangeAction", "modelChangeAction(robot,w)" ,"butlerresourcemodel" ) 
								startTimer()
						}
						stateTimer = TimerActor("timer_doMoveForward", 
							scope, context!!, "local_tout_onestep_doMoveForward", StepTime )
					}
					 transition(edgeName="t01",targetState="endDoMoveForward",cond=whenTimeout("local_tout_onestep_doMoveForward"))   
					transition(edgeName="t02",targetState="stepFail",cond=whenEvent("obstacleDetected"))
				}	 
				state("endDoMoveForward") { //this:State
					action { //it:State
						println("STEPDONE")
						forward("robotAction", "robotAction(h)" ,"butler" ) 
						forward("modelUpdateAction", "modelUpdateAction(robot,h)" ,"butlerresourcemodel" ) 
						forward("stepOk", "stepOk(ok)" ,"roomexplorationvirtual" ) 
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("stepFail") { //this:State
					action { //it:State
						forward("robotAction", "robotAction(h)" ,"butler" ) 
						forward("modelUpdateAction", "modelUpdateAction(robot,h)" ,"butlerresourcemodel" ) 
						forward("stepFail", "stepFail(fail)" ,"roomexplorationvirtual" ) 
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("sendDispatch") { //this:State
					action { //it:State
						forward("robotAction", "robotAction(h)" ,"butler" ) 
						forward("modelUpdateAction", "modelUpdateAction(robot,h)" ,"butlerresourcemodel" ) 
						forward("stepFail", "stepFail(b)" ,"roomexplorationvirtual" ) 
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}