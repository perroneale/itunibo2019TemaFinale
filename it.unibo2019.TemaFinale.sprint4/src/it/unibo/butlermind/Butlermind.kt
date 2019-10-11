/* Generated by AN DISI Unibo */ 
package it.unibo.butlermind

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Butlermind ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var Task = ""
			  var table = 0
			  var inRh = false
			  var position = "rh"
			  var Dishes = ""
			  var Posate = ""
			  var Bicchieri = ""
			  var FoodString =""
			  var NameReq = ""
			  var QuantityReq = ""
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						solve("consult('sysRules.pl')","") //set resVar	
						solve("consult('butlerMindKB.pl')","") //set resVar	
						solve("consult('configRoom.pl')","") //set resVar	
						println("ButlerMind STARTED")
						solve("dishes(X)","") //set resVar	
						Dishes = getCurSol("X").toString()
						solve("posate(Y)","") //set resVar	
						Dishes = getCurSol("Y").toString()
						solve("bicchieri(Z)","") //set resVar	
						Dishes = getCurSol("Z").toString()
						solve("getFood(L)","") //set resVar	
						FoodString = getCurSol("L").toString()
						itunibo.robot.foodRequire.setContent( FoodString  )
					}
					 transition(edgeName="t08",targetState="updateKB",cond=whenDispatch("updateKBbm"))
				}	 
				state("updateKB") { //this:State
					action { //it:State
						println("###BUTLERMIND")
						if( checkMsgContent( Term.createTerm("updateKBbm(LIST)"), Term.createTerm("updateKBbm(LIST)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								solve("addTable(${payloadArg(0)})","") //set resVar	
						}
					}
					 transition( edgeName="goto",targetState="waitCmd1", cond=doswitch() )
				}	 
				state("waitCmd1") { //this:State
					action { //it:State
						forward("modelChangeTask", "modelChangeTask(robot,waiting,0,0)" ,"butlerresourcemodel" ) 
					}
					 transition(edgeName="t09",targetState="preparing",cond=whenEvent("modelChangedpreparing"))
				}	 
				state("preparing") { //this:State
					action { //it:State
						println("---BUTLERMIND in preparing")
						Task = "preparing";
						forward("calculateRoute", "calculateRoute(pantry)" ,"planningroute" ) 
					}
					 transition(edgeName="t010",targetState="actionPrepare",cond=whenDispatch("destinationReached"))
				}	 
				state("actionPrepare") { //this:State
					action { //it:State
						println("###BUTLERMIND in actionPrepare")
						if( checkMsgContent( Term.createTerm("destinationReached(X,Y)"), Term.createTerm("destinationReached(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								solve("getPosition(${payloadArg(0)},${payloadArg(1)},POSITION)","") //set resVar	
								position = getCurSol("POSITION").toString()
								println(position)
						}
						if(position == "pantry"){ forward("getDishesPantry", "getDishesPantry($Dishes,$Bicchieri,$Posate)" ,"pantry" ) 
						delay(1000) 
						forward("calculateRoute", "calculateRoute(table)" ,"planningroute" ) 
						 }
						else
						 { if(position == "table" && table == 0){ forward("putDishesTable", "putDishes($Dishes,$Bicchieri,$Posate)" ,"table" ) 
						 forward("calculateRoute", "calculateRoute(fridge)" ,"planningroute" ) 
						 table++
						  }
						 else
						  { if(position == "fridge"){ var FoodCont = itunibo.robot.foodRequire.getFood()
						  itunibo.coap.client.butlerMindClient.requireAllFood( FoodCont  )
						  delay(2000) 
						  forward("calculateRoute", "calculateRoute(table)" ,"planningroute" ) 
						   }
						  else
						   { if(position == "table" && table == 1){ forward("calculateRoute", "calculateRoute(rh)" ,"planningroute" ) 
						   table = 0
						    }
						   else
						    { if(position == "rh"){ forward("completedTask", "completedTask($Task)" ,"maitre" ) 
						    forward("completedTask", "completedTask" ,"butlermind" ) 
						     }
						     }
						    }
						   }
						  }
					}
					 transition(edgeName="t011",targetState="actionPrepare",cond=whenDispatch("destinationReached"))
					transition(edgeName="t012",targetState="waitCmd2",cond=whenDispatch("completedTask"))
				}	 
				state("waitCmd2") { //this:State
					action { //it:State
						Task = ""
								  inRh = false
						position = "rh"
						println("###IN waitCmd2")
					}
					 transition(edgeName="t013",targetState="adding",cond=whenEvent("modelChangedadding"))
					transition(edgeName="t014",targetState="cleaning",cond=whenEvent("modelChangedcleaning"))
				}	 
				state("cleaning") { //this:State
					action { //it:State
						Task = "cleaning";
						forward("calculateRoute", "calculateRoute(table)" ,"planningroute" ) 
						position = "table"
					}
					 transition(edgeName="t015",targetState="actionClean",cond=whenDispatch("destinationReached"))
				}	 
				state("actionClean") { //this:State
					action { //it:State
						println("###BUTLERMIND in actionCleaning")
						if( checkMsgContent( Term.createTerm("destinationReached(X,Y)"), Term.createTerm("destinationReached(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								solve("getPosition(${payloadArg(0)},${payloadArg(1)},POSITION)","") //set resVar	
								position = getCurSol("POSITION").toString()
								println(position)
						}
						if(position == "dishwasher"){ forward("putDishesDish", "putDishes($Dishes,$Bicchieri,$Posate)" ,"dishwasher" ) 
						forward("calculateRoute", "calculateRoute(rh)" ,"planningroute" ) 
						 }
						else
						 { if(position == "table" && table == 0){ forward("calculateRoute", "calculateRoute(fridge)" ,"planningroute" ) 
						 table++
						  }
						 else
						  { if(position == "fridge"){ forward("calculateRoute", "calculateRoute(table)" ,"planningroute" ) 
						   }
						  else
						   { if(position == "table" && table == 1){ forward("getDishesTable", "getDishes($Dishes,$Bicchieri,$Posate)" ,"table" ) 
						   forward("calculateRoute", "calculateRoute(dishwasher)" ,"planningroute" ) 
						   table = 0
						    }
						   else
						    { if(position == "rh"){ forward("completedTask", "completedTask($Task)" ,"maitre" ) 
						    forward("completedTask", "completedTask" ,"butlermind" ) 
						     }
						     }
						    }
						   }
						  }
					}
					 transition(edgeName="t016",targetState="actionClean",cond=whenDispatch("destinationReached"))
					transition(edgeName="t017",targetState="waitCmd1",cond=whenDispatch("completedTask"))
				}	 
				state("adding") { //this:State
					action { //it:State
						Task = "adding"
						if( checkMsgContent( Term.createTerm("modelChangedadding(DEST,TASK,C,Q)"), Term.createTerm("modelChangedadding(robot,adding,C,Q)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								NameReq = payloadArg(2)
											  QuantityReq = payloadArg(3)
								itunibo.coap.client.butlerMindClient.foodAvailability(myself ,payloadArg(2), payloadArg(3) )
						}
					}
					 transition(edgeName="t018",targetState="nextStep",cond=whenDispatch("positiveResponse"))
					transition(edgeName="t019",targetState="sendWarning",cond=whenDispatch("negativeResponse"))
				}	 
				state("nextStep") { //this:State
					action { //it:State
						forward("calculateRoute", "calculateRoute(fridge)" ,"planningroute" ) 
					}
					 transition(edgeName="t020",targetState="actionAdd",cond=whenDispatch("destinationReached"))
				}	 
				state("actionAdd") { //this:State
					action { //it:State
						println("###BUTLERMIND in actionAdd")
						if( checkMsgContent( Term.createTerm("destinationReached(X,Y)"), Term.createTerm("destinationReached(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								solve("getPosition(${payloadArg(0)},${payloadArg(1)},POSITION)","") //set resVar	
								position = getCurSol("POSITION").toString()
								println(position)
						}
						if(position == "table"){ forward("calculateRoute", "calculateRoute(rh)" ,"planningroute" ) 
						 }
						else
						 { if(position == "fridge"){ itunibo.coap.client.butlerMindClient.takeOneFood( NameReq, QuantityReq  )
						 forward("calculateRoute", "calculateRoute(table)" ,"planningroute" ) 
						  }
						 else
						  { if(position == "rh"){ forward("completedTask", "completedTask()" ,"butlermind" ) 
						   }
						   }
						  }
					}
					 transition(edgeName="t021",targetState="actionAdd",cond=whenDispatch("destinationReached"))
					transition(edgeName="t022",targetState="waitCmd2",cond=whenDispatch("completedTask"))
				}	 
				state("sendWarning") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("negativeResponse(C,Q,AQ)"), Term.createTerm("negativeResponse(C,Q,AQ)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								forward("warning", "warning(${payloadArg(0)},${payloadArg(1)},${payloadArg(2)})" ,"maitre" ) 
						}
					}
					 transition( edgeName="goto",targetState="waitCmd2", cond=doswitch() )
				}	 
			}
		}
}
