/* Generated by AN DISI Unibo */ 
package it.unibo.butlerresourcemodel

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Butlerresourcemodel ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						solve("consult('sysRules.pl')","") //set resVar	
						solve("consult('resourceModelButler.pl')","") //set resVar	
						println("###RESOURCEMODEL STARTED---")
					}
					 transition( edgeName="goto",targetState="waitModelChange", cond=doswitch() )
				}	 
				state("waitModelChange") { //this:State
					action { //it:State
					}
					 transition(edgeName="t08",targetState="updateRobotType",cond=whenDispatch("robotType"))
					transition(edgeName="t09",targetState="handleModelChangeTask",cond=whenDispatch("modelChangeTask"))
					transition(edgeName="t010",targetState="handleModelChangeAction",cond=whenDispatch("modelChangeAction"))
					transition(edgeName="t011",targetState="handleModelChangePos",cond=whenDispatch("modelChangePos"))
					transition(edgeName="t012",targetState="updateModel",cond=whenDispatch("modelUpdateAction"))
				}	 
				state("updateModel") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("modelUpdateAction(DEST,ACTION)"), Term.createTerm("modelUpdateAction(robot,ACTION)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								itunibo.robot.resourceModelSupport.updateModel(myself ,payloadArg(1) )
						}
					}
					 transition( edgeName="goto",targetState="waitModelChange", cond=doswitch() )
				}	 
				state("handleModelChangeTask") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("modelChangeTask(DEST,TASK,IDCIBO,QUANTITY)"), Term.createTerm("modelChangeTask(robot,TASK,IDCIBO,QUANTITY)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								itunibo.robot.resourceModelSupport.updateModelTask(myself ,payloadArg(1), payloadArg(2), payloadArg(3) )
						}
					}
					 transition( edgeName="goto",targetState="waitModelChange", cond=doswitch() )
				}	 
				state("handleModelChangeAction") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("modelChangeAction(DEST,ACTION)"), Term.createTerm("modelChangeAction(robot,ACTION)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("Butlerresourcemodel in handleAction")
								itunibo.robot.resourceModelSupport.updateModelAction(myself ,payloadArg(1) )
						}
					}
					 transition( edgeName="goto",targetState="waitModelChange", cond=doswitch() )
				}	 
				state("handleModelChangePos") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("modelChangePos(DEST,X,Y)"), Term.createTerm("modelChangePos(robot,X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("Butlerresourcemodel in handleModelChangePos")
								itunibo.robot.resourceModelSupport.updateModelPosition(myself ,payloadArg(1), payloadArg(2) )
						}
					}
					 transition( edgeName="goto",targetState="waitModelChange", cond=doswitch() )
				}	 
				state("updateRobotType") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("robotType(TYPE)"), Term.createTerm("robotType(ROBOT)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								var Robot = "" 
											Robot = payloadArg(0)
								println("add robottype")
								solve("assert(robotType($Robot))","") //set resVar	
								solve("robotType(R)","") //set resVar	
								println("${getCurSol("R").toString()}")
						}
					}
					 transition( edgeName="goto",targetState="waitModelChange", cond=doswitch() )
				}	 
			}
		}
}
