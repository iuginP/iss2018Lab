/* Generated by AN DISI Unibo */ 
package it.unibo.ctxRealRobotExecutor;
import it.unibo.qactors.QActorContext;
import it.unibo.is.interfaces.IOutputEnvView;
import it.unibo.system.SituatedSysKb;
public class MainCtxRealRobotExecutor  {
  
//MAIN
public static QActorContext initTheContext() throws Exception{
	IOutputEnvView outEnvView = SituatedSysKb.standardOutEnvView;
	String webDir = "./srcMore/it/unibo/ctxRealRobotExecutor";
	return QActorContext.initQActorSystem(
		"ctxrealrobotexecutor", "./srcMore/it/unibo/ctxRealRobotExecutor/realrobotexecutor.pl", 
		"./srcMore/it/unibo/ctxRealRobotExecutor/sysRules.pl", outEnvView,webDir,false);
}
public static void main(String[] args) throws Exception{
	QActorContext ctx = initTheContext();
} 	
}