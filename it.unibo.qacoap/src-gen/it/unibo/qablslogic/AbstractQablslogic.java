/* Generated by AN DISI Unibo */ 
package it.unibo.qablslogic;
import it.unibo.qactors.PlanRepeat;
import it.unibo.qactors.QActorContext;
import it.unibo.qactors.StateExecMessage;
import it.unibo.qactors.QActorUtils;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.qactors.action.AsynchActionResult;
import it.unibo.qactors.action.IActorAction;
import it.unibo.qactors.action.IActorAction.ActionExecMode;
import it.unibo.qactors.action.IMsgQueue;
import it.unibo.qactors.akka.QActor;
import it.unibo.qactors.StateFun;
import java.util.Stack;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import it.unibo.qactors.action.ActorTimedAction;
public abstract class AbstractQablslogic extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	 
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractQablslogic(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/qablslogic/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/qablslogic/plans.txt";
	  	}
		@Override
		protected void doJob() throws Exception {
			String name  = getName().replace("_ctrl", "");
			mysupport = (IMsgQueue) QActorUtils.getQActor( name ); 
			initStateTable(); 
	 		initSensorSystem();
	 		history.push(stateTab.get( "init" ));
	  	 	autoSendStateExecMsg();
	  		//QActorContext.terminateQActorSystem(this);//todo
		} 	
		/* 
		* ------------------------------------------------------------
		* PLANS
		* ------------------------------------------------------------
		*/    
	    //genAkkaMshHandleStructure
	    protected void initStateTable(){  	
	    	stateTab.put("handleToutBuiltIn",handleToutBuiltIn);
	    	stateTab.put("init",init);
	    	stateTab.put("handleLedAsCoapClient",handleLedAsCoapClient);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "qablslogic tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_init",0);
	     pr.incNumIter(); 	
	    	String myselfName = "init";  
	    	temporaryStr = "\"qablslogic STARTS\"";
	    	println( temporaryStr );  
	    	//bbb
	     msgTransition( pr,myselfName,"qablslogic_"+myselfName,false,
	          new StateFun[]{stateTab.get("handleLedAsCoapClient") }, 
	          new String[]{"true","E","usercmd" },
	          6000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun handleLedAsCoapClient = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleLedAsCoapClient",-1);
	    	String myselfName = "handleLedAsCoapClient";  
	    	printCurrentEvent(false);
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(true)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			//println("WARNING: variable substitution not yet fully implemented " ); 
	    			doCoapPost( "localhost:8010/led", "true" );
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("usercmd(false)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("usercmd") && 
	    		pengine.unify(curT, Term.createTerm("usercmd(X)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			//println("WARNING: variable substitution not yet fully implemented " ); 
	    			doCoapPost( "localhost:8010/led", "false" );
	    	}
	    	repeatPlanNoTransition(pr,myselfName,"qablslogic_"+myselfName,false,true);
	    }catch(Exception e_handleLedAsCoapClient){  
	    	 println( getName() + " plan=handleLedAsCoapClient WARNING:" + e_handleLedAsCoapClient.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleLedAsCoapClient
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}