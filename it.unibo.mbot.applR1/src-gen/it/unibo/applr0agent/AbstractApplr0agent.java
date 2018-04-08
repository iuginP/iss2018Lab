/* Generated by AN DISI Unibo */ 
package it.unibo.applr0agent;
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
public abstract class AbstractApplr0agent extends QActor { 
	protected AsynchActionResult aar = null;
	protected boolean actionResult = true;
	protected alice.tuprolog.SolveInfo sol;
	protected String planFilePath    = null;
	protected String terminationEvId = "default";
	protected String parg="";
	protected boolean bres=false;
	protected IActorAction action;
	//protected String mqttServer = "tcp://localhost:1883";
	
		protected static IOutputEnvView setTheEnv(IOutputEnvView outEnvView ){
			return outEnvView;
		}
		public AbstractApplr0agent(String actorId, QActorContext myCtx, IOutputEnvView outEnvView )  throws Exception{
			super(actorId, myCtx,  
			"./srcMore/it/unibo/applr0agent/WorldTheory.pl",
			setTheEnv( outEnvView )  , "init");
			this.planFilePath = "./srcMore/it/unibo/applr0agent/plans.txt";
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
	    	stateTab.put("doWork",doWork);
	    	stateTab.put("handleSonarEvents",handleSonarEvents);
	    	stateTab.put("checkMobileObstacle",checkMobileObstacle);
	    	stateTab.put("fixedObstacle",fixedObstacle);
	    	stateTab.put("robotAtSonar1",robotAtSonar1);
	    	stateTab.put("robotAtSonar2",robotAtSonar2);
	    	stateTab.put("redoWork",redoWork);
	    	stateTab.put("adjustRobotPosition",adjustRobotPosition);
	    	stateTab.put("checkPos",checkPos);
	    	stateTab.put("doWaitAnswer",doWaitAnswer);
	    	stateTab.put("checkTheAnswer",checkTheAnswer);
	    	stateTab.put("noAnswer",noAnswer);
	    	stateTab.put("alarmHandlePolicy",alarmHandlePolicy);
	    }
	    StateFun handleToutBuiltIn = () -> {	
	    	try{	
	    		PlanRepeat pr = PlanRepeat.setUp("handleTout",-1);
	    		String myselfName = "handleToutBuiltIn";  
	    		println( "applr0agent tout : stops");  
	    		repeatPlanNoTransition(pr,myselfName,"application_"+myselfName,false,false);
	    	}catch(Exception e_handleToutBuiltIn){  
	    		println( getName() + " plan=handleToutBuiltIn WARNING:" + e_handleToutBuiltIn.getMessage() );
	    		QActorContext.terminateQActorSystem(this); 
	    	}
	    };//handleToutBuiltIn
	    
	    StateFun init = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("init",-1);
	    	String myselfName = "init";  
	     connectToMqttServer("tcp://localhost:1883");
	    	//switchTo doWork
	        switchToPlanAsNextState(pr, myselfName, "applr0agent_"+myselfName, 
	              "doWork",false, false, null); 
	    }catch(Exception e_init){  
	    	 println( getName() + " plan=init WARNING:" + e_init.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//init
	    
	    StateFun doWork = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp(getName()+"_doWork",0);
	     pr.incNumIter(); 	
	    	String myselfName = "doWork";  
	    	temporaryStr = "\"applr0agent doWork\"";
	    	println( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(w(low)))", guardVars ).toString();
	    	emit( "mindcmd", temporaryStr );
	    	//bbb
	     msgTransition( pr,myselfName,"applr0agent_"+myselfName,false,
	          new StateFun[]{stateTab.get("alarmHandlePolicy"), stateTab.get("handleSonarEvents") }, 
	          new String[]{"true","M","alarmmsg", "true","E","sonarSensor" },
	          6000000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_doWork){  
	    	 println( getName() + " plan=doWork WARNING:" + e_doWork.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doWork
	    
	    StateFun handleSonarEvents = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("handleSonarEvents",-1);
	    	String myselfName = "handleSonarEvents";  
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonar(sonar1,DISTANCE)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonarSensor") && 
	    		pengine.unify(curT, Term.createTerm("sonar(NAME,DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="atsonar1(DISTANCE)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("sonar(NAME,DISTANCE)"),  Term.createTerm("sonar(sonar1,DISTANCE)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("atsonar1","applr0agent", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonar(sonar2,DISTANCE)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonarSensor") && 
	    		pengine.unify(curT, Term.createTerm("sonar(NAME,DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="atsonar2(DISTANCE)";
	    			/* SendDispatch */
	    			parg = updateVars(Term.createTerm("sonar(NAME,DISTANCE)"),  Term.createTerm("sonar(sonar2,DISTANCE)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) sendMsg("atsonar2","applr0agent", QActorContext.dispatch, parg ); 
	    	}
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonar(roversonar,DISTANCE)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonarSensor") && 
	    		pengine.unify(curT, Term.createTerm("sonar(NAME,DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			//println("WARNING: variable substitution not yet fully implemented " ); 
	    			{//actionseq
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(h(low)))", guardVars ).toString();
	    			emit( "mindcmd", temporaryStr );
	    			temporaryStr = "sonar(roversonar)";
	    			println( temporaryStr );  
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(d(low)))", guardVars ).toString();
	    			emit( "mindcmd", temporaryStr );
	    			temporaryStr = QActorUtils.unifyMsgContent(pengine,"hopemobile(ARG)","hopemobile(\"\")", guardVars ).toString();
	    			sendMsg("hopemobile","applr0agent", QActorContext.dispatch, temporaryStr ); 
	    			};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"applr0agent_"+myselfName,true,
	          new StateFun[]{stateTab.get("checkMobileObstacle"), stateTab.get("robotAtSonar1"), stateTab.get("robotAtSonar2") }, 
	          new String[]{"true","M","hopemobile", "true","M","atsonar1", "true","M","atsonar2" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_handleSonarEvents){  
	    	 println( getName() + " plan=handleSonarEvents WARNING:" + e_handleSonarEvents.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//handleSonarEvents
	    
	    StateFun checkMobileObstacle = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("checkMobileObstacle",-1);
	    	String myselfName = "checkMobileObstacle";  
	    	temporaryStr = "\"checkMobileObstacle\"";
	    	println( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(a(low)))", guardVars ).toString();
	    	emit( "mindcmd", temporaryStr );
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(1000,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "checkMobileObstacle";
	    	if( ! aar.getGoon() ) return ;
	    	//bbb
	     msgTransition( pr,myselfName,"applr0agent_"+myselfName,false,
	          new StateFun[]{stateTab.get("fixedObstacle") }, 
	          new String[]{"true","E","sonarSensor" },
	          1000, "doWork" );//msgTransition
	    }catch(Exception e_checkMobileObstacle){  
	    	 println( getName() + " plan=checkMobileObstacle WARNING:" + e_checkMobileObstacle.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//checkMobileObstacle
	    
	    StateFun fixedObstacle = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("fixedObstacle",-1);
	    	String myselfName = "fixedObstacle";  
	    	temporaryStr = "\"fixedobstaclee\"";
	    	println( temporaryStr );  
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " !?task(tasktodo(obstacleAvoidance,on))" )) != null ){
	    	{//actionseq
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(d(low)))", guardVars ).toString();
	    	emit( "mindcmd", temporaryStr );
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(700,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "fixedObstacle";
	    	if( ! aar.getGoon() ) return ;
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "tasktodo(TASK,ARGS)","tasktodo(obstacleAvoidance,\"do\")", guardVars ).toString();
	    	emit( "tasktodo", temporaryStr );
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"waitAnswer(ARG)","waitAnswer(\"obstacleAvoidance\")", guardVars ).toString();
	    	sendMsg("waitAnswer","applr0agent", QActorContext.dispatch, temporaryStr ); 
	    	};//actionseq
	    	}
	    	else{ {//actionseq
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(s(low)))", guardVars ).toString();
	    	emit( "mindcmd", temporaryStr );
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(800,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "fixedObstacle";
	    	if( ! aar.getGoon() ) return ;
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(h(low)))", guardVars ).toString();
	    	emit( "mindcmd", temporaryStr );
	    	temporaryStr = "\"SORRY: fixed obstacle without strategy\"";
	    	println( temporaryStr );  
	    	};//actionseq
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"applr0agent_"+myselfName,false,
	          new StateFun[]{stateTab.get("doWaitAnswer"), stateTab.get("doWork") }, 
	          new String[]{"true","M","waitAnswer", "true","E","alarmev" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_fixedObstacle){  
	    	 println( getName() + " plan=fixedObstacle WARNING:" + e_fixedObstacle.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//fixedObstacle
	    
	    StateFun robotAtSonar1 = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("robotAtSonar1",-1);
	    	String myselfName = "robotAtSonar1";  
	    	printCurrentMessage(false);
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("atsonar1(D)");
	    	if( currentMessage != null && currentMessage.msgId().equals("atsonar1") && 
	    		pengine.unify(curT, Term.createTerm("atsonar1(DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="sonar1ok(D)";
	    		/* AddRule */
	    		parg = updateVars(Term.createTerm("atsonar1(DISTANCE)"),  Term.createTerm("atsonar1(D)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) addRule(parg);	    		  					
	    	}
	    	//switchTo doWork
	        switchToPlanAsNextState(pr, myselfName, "applr0agent_"+myselfName, 
	              "doWork",false, false, null); 
	    }catch(Exception e_robotAtSonar1){  
	    	 println( getName() + " plan=robotAtSonar1 WARNING:" + e_robotAtSonar1.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//robotAtSonar1
	    
	    StateFun robotAtSonar2 = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("robotAtSonar2",-1);
	    	String myselfName = "robotAtSonar2";  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(h(low)))", guardVars ).toString();
	    	emit( "mindcmd", temporaryStr );
	    	//onMsg 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("atsonar2(D)");
	    	if( currentMessage != null && currentMessage.msgId().equals("atsonar2") && 
	    		pengine.unify(curT, Term.createTerm("atsonar2(DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentMessage.msgContent() ) )){ 
	    		String parg="sonar2ok(D)";
	    		/* AddRule */
	    		parg = updateVars(Term.createTerm("atsonar2(DISTANCE)"),  Term.createTerm("atsonar2(D)"), 
	    			    		  					Term.createTerm(currentMessage.msgContent()), parg);
	    		if( parg != null ) addRule(parg);	    		  					
	    	}
	    	parg = "robotFinalPosition(RES)";
	    	//QActorUtils.solveGoal(myself,parg,pengine );  //sets currentActionResult		
	    	solveGoal( parg ); //sept2017
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??goalResult(robotFinalPosition(wrong))" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"updatePos(ARG)","updatePos(\"\")", guardVars ).toString();
	    	sendMsg("updatePos","applr0agent", QActorContext.dispatch, temporaryStr ); 
	    	}
	    	if( (guardVars = QActorUtils.evalTheGuard(this, " ??goalResult(robotFinalPosition(ok))" )) != null ){
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine,"redoWork(ARG)","redoWork(\"\")", guardVars ).toString();
	    	sendMsg("redoWork","applr0agent", QActorContext.dispatch, temporaryStr ); 
	    	}
	    	//bbb
	     msgTransition( pr,myselfName,"applr0agent_"+myselfName,false,
	          new StateFun[]{stateTab.get("adjustRobotPosition"), stateTab.get("redoWork") }, 
	          new String[]{"true","M","updatePos", "true","M","redoWork" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_robotAtSonar2){  
	    	 println( getName() + " plan=robotAtSonar2 WARNING:" + e_robotAtSonar2.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//robotAtSonar2
	    
	    StateFun redoWork = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("redoWork",-1);
	    	String myselfName = "redoWork";  
	    	temporaryStr = "\"REPOSITION THE ROBOT AND PRESS FIRE Button\"";
	    	println( temporaryStr );  
	    	//bbb
	     msgTransition( pr,myselfName,"applr0agent_"+myselfName,false,
	          new StateFun[]{stateTab.get("doWork") }, 
	          new String[]{"true","E","alarmev" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_redoWork){  
	    	 println( getName() + " plan=redoWork WARNING:" + e_redoWork.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//redoWork
	    
	    StateFun adjustRobotPosition = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("adjustRobotPosition",-1);
	    	String myselfName = "adjustRobotPosition";  
	    	temporaryStr = "\"going at place .... \"";
	    	println( temporaryStr );  
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(a(low)))", guardVars ).toString();
	    	emit( "mindcmd", temporaryStr );
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(400,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "adjustRobotPosition";
	    	if( ! aar.getGoon() ) return ;
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(w(low)))", guardVars ).toString();
	    	emit( "mindcmd", temporaryStr );
	    	//delay  ( no more reactive within a plan)
	    	aar = delayReactive(1500,"" , "");
	    	if( aar.getInterrupted() ) curPlanInExec   = "adjustRobotPosition";
	    	if( ! aar.getGoon() ) return ;
	    	temporaryStr = QActorUtils.unifyMsgContent(pengine, "usercmd(CMD)","usercmd(robotgui(d(low)))", guardVars ).toString();
	    	emit( "mindcmd", temporaryStr );
	    	//bbb
	     msgTransition( pr,myselfName,"applr0agent_"+myselfName,false,
	          new StateFun[]{stateTab.get("checkPos") }, 
	          new String[]{"true","E","sonarSensor" },
	          60000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_adjustRobotPosition){  
	    	 println( getName() + " plan=adjustRobotPosition WARNING:" + e_adjustRobotPosition.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//adjustRobotPosition
	    
	    StateFun checkPos = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("checkPos",-1);
	    	String myselfName = "checkPos";  
	    	printCurrentEvent(false);
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("sonar(sonar2,D)");
	    	if( currentEvent != null && currentEvent.getEventId().equals("sonarSensor") && 
	    		pengine.unify(curT, Term.createTerm("sonar(NAME,DISTANCE)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="sonar2ok(D)";
	    			/* AddRule */
	    			parg = updateVars(Term.createTerm("sonar(NAME,DISTANCE)"),  Term.createTerm("sonar(sonar2,D)"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) addRule(parg);	    		  					
	    	}
	    	//switchTo robotAtSonar2
	        switchToPlanAsNextState(pr, myselfName, "applr0agent_"+myselfName, 
	              "robotAtSonar2",false, false, null); 
	    }catch(Exception e_checkPos){  
	    	 println( getName() + " plan=checkPos WARNING:" + e_checkPos.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//checkPos
	    
	    StateFun doWaitAnswer = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("doWaitAnswer",-1);
	    	String myselfName = "doWaitAnswer";  
	    	temporaryStr = "\"applr0agent WAITS that a passage is found\"";
	    	println( temporaryStr );  
	    	//bbb
	     msgTransition( pr,myselfName,"applr0agent_"+myselfName,false,
	          new StateFun[]{stateTab.get("checkTheAnswer") }, 
	          new String[]{"true","E","taskdone" },
	          60000, "noAnswer" );//msgTransition
	    }catch(Exception e_doWaitAnswer){  
	    	 println( getName() + " plan=doWaitAnswer WARNING:" + e_doWaitAnswer.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//doWaitAnswer
	    
	    StateFun checkTheAnswer = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("checkTheAnswer",-1);
	    	String myselfName = "checkTheAnswer";  
	    	printCurrentEvent(false);
	    	//onEvent 
	    	setCurrentMsgFromStore(); 
	    	curT = Term.createTerm("taskdone(obstacleAvoidance,\"ok\")");
	    	if( currentEvent != null && currentEvent.getEventId().equals("taskdone") && 
	    		pengine.unify(curT, Term.createTerm("taskdone(TASK,ARGS)")) && 
	    		pengine.unify(curT, Term.createTerm( currentEvent.getMsg() ) )){ 
	    			String parg="goon";
	    			/* AddRule */
	    			parg = updateVars(Term.createTerm("taskdone(TASK,ARGS)"),  Term.createTerm("taskdone(obstacleAvoidance,\"ok\")"), 
	    				    		  					Term.createTerm(currentEvent.getMsg()), parg);
	    			if( parg != null ) addRule(parg);	    		  					
	    	}
	    	//switchTo doWork
	        switchToPlanAsNextState(pr, myselfName, "applr0agent_"+myselfName, 
	              "doWork",false, true, " ??goon"); 
	    }catch(Exception e_checkTheAnswer){  
	    	 println( getName() + " plan=checkTheAnswer WARNING:" + e_checkTheAnswer.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//checkTheAnswer
	    
	    StateFun noAnswer = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("noAnswer",-1);
	    	String myselfName = "noAnswer";  
	    	temporaryStr = "\"applr0agent no answer received. Press FIRE to restart\"";
	    	println( temporaryStr );  
	    	//bbb
	     msgTransition( pr,myselfName,"applr0agent_"+myselfName,false,
	          new StateFun[]{stateTab.get("doWork") }, 
	          new String[]{"true","E","alarmev" },
	          600000, "handleToutBuiltIn" );//msgTransition
	    }catch(Exception e_noAnswer){  
	    	 println( getName() + " plan=noAnswer WARNING:" + e_noAnswer.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//noAnswer
	    
	    StateFun alarmHandlePolicy = () -> {	
	    try{	
	     PlanRepeat pr = PlanRepeat.setUp("alarmHandlePolicy",-1);
	    	String myselfName = "alarmHandlePolicy";  
	    	printCurrentMessage(false);
	    	temporaryStr = "\"applr0agent ALARM HANDLING POLICY DONE \"";
	    	println( temporaryStr );  
	    	repeatPlanNoTransition(pr,myselfName,"applr0agent_"+myselfName,false,true);
	    }catch(Exception e_alarmHandlePolicy){  
	    	 println( getName() + " plan=alarmHandlePolicy WARNING:" + e_alarmHandlePolicy.getMessage() );
	    	 QActorContext.terminateQActorSystem(this); 
	    }
	    };//alarmHandlePolicy
	    
	    protected void initSensorSystem(){
	    	//doing nothing in a QActor
	    }
	
	}
