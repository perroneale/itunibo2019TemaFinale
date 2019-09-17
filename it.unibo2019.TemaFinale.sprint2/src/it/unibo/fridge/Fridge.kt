/* Generated by AN DISI Unibo */ 
package it.unibo.fridge

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Fridge ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		var Quantity = 0;
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
					}
				}	 
				state("waitCmd") { //this:State
					action { //it:State
					}
					 transition(edgeName="t039",targetState="checking",cond=whenDispatch("foodAvailability"))
					transition(edgeName="t040",targetState="updatingTf",cond=whenDispatch("foodTaken"))
					transition(edgeName="t041",targetState="updatingPf",cond=whenDispatch("foodPutted"))
				}	 
				state("checking") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("foodAvailability(C,Q)"), Term.createTerm("foodAvailability(Code,Quant)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								solve("food(${payloadArg(0)},Q)","") //set resVar	
								if(currentSolution.isSuccess()) { Quantity = getCurSol("Q").toString().toInt()
								 }
								if(Quantity <= payloadArg(1).toString().toInt()){ forward("positiveResponse", "positiveResponse" ,"butler" ) 
								 }
								else
								 { forward("negativeResponse", "negativeResponse(${payloadArg(0)},${payloadArg(1)},$Quantity)" ,"butler" ) 
								  }
						}
					}
					 transition( edgeName="goto",targetState="waitCmd", cond=doswitch() )
				}	 
				state("updatingTf") { //this:State
					action { //it:State
						var QA = 0; 
								  var Diff = 0
						if( checkMsgContent( Term.createTerm("foodTaken(C,Q)"), Term.createTerm("foodTaken(Code,Quant)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								solve("food(${payloadArg(0)},QA)","") //set resVar	
								if(currentSolution.isSuccess()) { QA = getCurSol("QA").toString().toInt();
												  Diff = QA - payloadArg(1).toString().toInt()
								 }
								solve("updateQuantity(${payloadArg(0)},$Diff)","") //set resVar	
						}
					}
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
				state("updatingPf") { //this:State
					action { //it:State
						var QA = 0; 
								  var Diff = 0
						if( checkMsgContent( Term.createTerm("foodPutted(C,Q)"), Term.createTerm("foodPutted(Code,Quant)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								solve("food(${payloadArg(0)},QA)","") //set resVar	
								if(currentSolution.isSuccess()) { QA = getCurSol("QA").toString().toInt();
												  Diff = QA + payloadArg(1).toString().toInt()
								 }
								solve("updateQuantity(${payloadArg(0)},$Diff)","") //set resVar	
						}
					}
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}
