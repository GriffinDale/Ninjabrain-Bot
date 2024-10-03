package ninjabrainbot.model.information;

import ninjabrainbot.event.IObservable;
import ninjabrainbot.io.preferences.NinjabrainBotPreferences;
import ninjabrainbot.model.datastate.IDataState;
import ninjabrainbot.model.datastate.calculator.ICalculatorResult;
import ninjabrainbot.model.datastate.common.ResultType;
import ninjabrainbot.model.datastate.endereye.IEnderEyeThrow;
import ninjabrainbot.model.datastate.stronghold.ChunkPrediction;
import ninjabrainbot.model.environmentstate.IEnvironmentState;
import ninjabrainbot.model.environmentstate.StandardDeviationSettings;
import ninjabrainbot.util.I18n;

public class DistanceWarningProvider extends InformationMessageProvider {

    private final IDataState dataState;
    private final IObservable<StandardDeviationSettings> standardDeviationSettings;

    public DistanceWarningProvider(IDataState dataState, IEnvironmentState environmentState, NinjabrainBotPreferences preferences) {
        super(preferences.informationMismeasureEnabled);
        this.dataState = dataState;
        this.standardDeviationSettings = environmentState.standardDeviationSettings();
        disposeHandler.add(dataState.calculatorResult().subscribe(this::raiseInformationMessageChanged));
        disposeHandler.add(dataState.resultType().subscribe(this::raiseInformationMessageChanged));
        disposeHandler.add(standardDeviationSettings.subscribe(this::raiseInformationMessageChanged));
    }

    @Override
    protected boolean shouldShowInformationMessage() {
        if (dataState.resultType().get() != ResultType.TRIANGULATION)
            return false;

        ICalculatorResult calculatorResult = dataState.calculatorResult().get();
        if (calculatorResult == null || !calculatorResult.success())
            return false;

        ChunkPrediction bestPrediction = calculatorResult.getBestPrediction();
        double likelihood = 1;
        double expectedLikelihood = 1;
        for (IEnderEyeThrow t : dataState.getThrowList()) {
            double error = bestPrediction.getAngleError(t);
            double sigma = t.getStandardDeviation(standardDeviationSettings.get());
            likelihood *= Math.exp(-0.5 * (error / sigma) * (error / sigma));
            expectedLikelihood *= 1.0 / Math.sqrt(2);
        }

        System.out.println(bestPrediction.chunk.weight);
        if (bestPrediction.chunk.weight >= 0.3) {
            return false;
        }
        System.out.println(bestPrediction.getOverworldDistance() >= 9000);
        return bestPrediction.getOverworldDistance() >= 9000;
    }

    private InformationMessage warningMessage = null;

    @Override
    protected InformationMessage getInformationMessage() {
        if (warningMessage == null)
            warningMessage = new InformationMessage(InformationType.Warning, I18n.get("information.too_far"));
        return warningMessage;
    }

}
