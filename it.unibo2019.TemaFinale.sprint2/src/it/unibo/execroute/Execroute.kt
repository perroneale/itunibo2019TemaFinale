/* Generated by AN DISI Unibo */ 
package it.unibo.execroute

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Execroute ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var Curmove ="";
			  var nextState =""; 
			  var table = 0;
			  var Position = ""; 
			  var PosTable = "";
			  var Task = "";
			  var Duration = 0
			  var X= ""
			  var Y = ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("###EXECROUTE STARTED")
						solve("consult('sysRules.pl')","") //set resVar	
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
					}
					 transition(edgeName="t025",targetState="execRoute",cond=whenDispatch("exec"))
				}	 
				state("execRoute") { //this:State
					action { //it:State
						println("###EXECROUTE STATE EXEC ROUTE")
						solve("retract(move(M))","") //set resVar	
						if(currentSolution.isSuccess()) { Curmove = getCurSol("M").toString()
						 }
						else
						{ Curmove="nomove" 
						 }
						if((Curmove != "nomove")){ startTimer()
						itunibo.planner.moveUtils.execMove(myself ,Curmove )
						forward("nextMove", "nextMove" ,"execroute" ) 
						 }
						else
						 { forward("check", "check" ,"execroute" ) 
						  }
						X = itunibo.planner.moveUtils.getPosX(myself).toString()
								  Y = itunibo.planner.moveUtils.getPosY(myself).toString()
						forward("modelChangePos", "modelChangePos(robot,$X,$Y)" ,"butlerresourcemodel" ) 
					}
					 transition(edgeName="t026",targetState="stopApplication",cond=whenDispatch("stop"))
					transition(edgeName="t027",targetState="execRoute",cond=whenDispatch("nextMove"))
					transition(edgeName="t028",targetState="execRouteCompleted",cond=whenDispatch("check"))
					transition(edgeName="t029",targetState="handleObstacle",cond=whenEvent("obstacleDetected"))
				}	 
				state("execRouteCompleted") { //this:State
					action { //it:State
						forward("destinationReached", "destinationReached($X,$Y)" ,"butlermind" ) 
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("handleObstacle") { //this:State
					action { //it:State
						Duration = getDuration()
					}
					 transition( edgeName="goto",targetState="checkObstacle", cond=doswitch() )
				}	 
				state("checkObstacle") { //this:State
					action { //it:State
						forward("isObstacle", "isObstacle" ,"mind" ) 
					}
					 transition(edgeName="t030",targetState="checkObstacle",cond=whenDispatch("obstacle"))
					transition(edgeName="t031",targetState="completeStep",cond=whenDispatch("notObstacle"))
				}	 
				state("completeStep") { //this:State
					action { //it:State
						var stepTime = 770 - Duration
						itunibo.planner.moveUtils.moveAheadWithoutUpdate(myself ,stepTime )
					}
					 transition( edgeName="goto",targetState="execRoute", cond=doswitch() )
				}	 
				state("stopApplication") { //this:State
					action { //it:State
						println("###EXECROUTE stopped")
						forward("modelChangeAction", "modelChangeAction(robot,h)" ,"butlerresourcemodel" ) 
					}
					 transition(edgeName="t032",targetState="execRoute",cond=whenDispatch("reactivate"))
				}	 
			}
		}
}
