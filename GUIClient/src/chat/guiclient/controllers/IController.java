package chat.guiclient.controllers;

/**
 * Created by alex on 12/7/14.
 */
public interface IController {
    /**
     * В этом методе должно происходить обращение к ServiceLocator для получения зависимостей.
     */
    public void injectDependencies();
}
