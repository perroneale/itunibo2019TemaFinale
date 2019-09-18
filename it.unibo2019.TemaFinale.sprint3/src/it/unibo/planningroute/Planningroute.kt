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
		var set = false
			  var mapEmpty = true
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("###PLANNINGROUTE STARTED")
						solve("consult('sysRules.pl')","") //set resVar	
						solve("consult('planningRouteKB.pl')","") //set resVar	
						solve("consult('nearTable.pl')","") //set resVar	
						itunibo.planner.plannerUtil.initAI(  )
					}
					 transition( edgeName="goto",targetState="loadMap", cond=doswitch() )
				}	 
				state("loadMap") { //this:State
					action { //it:State
						solve("map(X)","") //set resVar	
						if(currentSolution.isSuccess()) { itunibo.planner.moveUtils.loadRoomMap(myself ,getCurSol("X").toString() )
						itunibo.planner.moveUtils.addTable(myself)
						println("map")
									  println(itunibo.planner.plannerUtil.getMap())
						solve("findall(nearTable(X,Y,table),nearTable(X,Y,_),L)","") //set resVar	
						var List = getCurSol("L").toString()
						println(List)
						forward("updateKBbm", "updateKBbm($List)" ,"butlermind" ) 
						 }
						else
						{ println("!!!---Error map name not setted---!!!")
						 }
						 mapEmpty = itunibo.planner.moveUtils.mapIsEmpty()
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitchGuarded({(mapEmpty == false)}) )
					transition( edgeName="goto",targetState="doExploration", cond=doswitchGuarded({! (mapEmpty == false)}) )
				}	 
				state("doExploration") { //this:State
					action { //it:State
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("createMap") { //this:State
					action { //it:State
						println("###IN CREATE MAP  butlerresourcemodel")
						if( checkMsgContent( Term.createTerm("map(MAPSTRING,MAPNAME)"), Term.createTerm("map(MAPSTRING,MAPNAME)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("$name in ${currentState.stateName} | $currentMsg")
								itunibo.planner.plannerUtil.saveMap( payloadArg(0), payloadArg(1)  )
								itunibo.planner.moveUtils.loadRoomMap(myself ,payloadArg(1) )
								itunibo.planner.moveUtils.addTable(myself)
								println("map")
											  println(itunibo.planner.plannerUtil.getMap())
											  set = true
								solve("findall(nearTable(X,Y,Pos),nearTable(X,Y,Pos),L)","") //set resVar	
								var List = getCurSol("L").toString()
								forward("updateKBbm", "updateKBbm($List)" ,"butlermind" ) 
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("waitCmd") { //this:State
					action { //it:State
					}
					 transition(edgeName="t022",targetState="createMap",cond=whenEventGuarded("map",{(set == false)}))
					transition(edgeName="t023",targetState="planningRoute",cond=whenDispatch("calculateRoute"))
					transition(edgeName="t024",targetState="updating",cond=whenEvent("modelChangedPosition"))
				}	 
				state("updating") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("modelChangedPosition(X,Y)"), Term.createTerm("modelChangedPosition(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("updating")
								solve("replaceRule(curPos(_,_),curPos(${payloadArg(0)},${payloadArg(1)}))","") //set resVar	
								solve("curPos(A,B)","") //set resVar	
								println(getCurSol("A").toString())
								println(getCurSol("B").toString())
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("planningRoute") { //this:State
					action { //it:State
						var x =""
								  var y =""
								  var dir =""
								  var dest = "execroute"
						if( checkMsgContent( Term.createTerm("calculateRoute(X)"), Term.createTerm("calculateRoute(GOAL)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								println("###PLANNINGROUTE calculate route to ${payloadArg(0)}")
								if(payloadArg(0) == "pantry"){ solve("pantry(X,Y,DIR)","") //set resVar	
								 }
								else
								 { if(payloadArg(0) == "dishwasher"){ solve("dishwasher(X,Y,DIR)","") //set resVar	
								  }
								 else
								  { if(payloadArg(0) == "fridge"){ solve("fridge(X,Y,DIR)","") //set resVar	
								   }
								  else
								   { if(payloadArg(0) == "rh"){ solve("rh(X,Y,DIR)","") //set resVar	
								    }
								   else
								    { if(payloadArg(0) == "table"){ solve("distance(X,Y,DIR)","") //set resVar	
								     }
								     }
								    }
								   }
								  }
								x = getCurSol("X").toString()
											  println(x)
											  y = getCurSol("Y").toString()
									          println(y)
											  dir = getCurSol("DIR").toString()
											  println(dir)
								itunibo.planner.moveUtils.setGoal(myself ,x, y, dir )
								itunibo.planner.moveUtils.doPlan( dest  )
								forward("exec", "exec" ,"execroute" ) 
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
			}
		}
}
