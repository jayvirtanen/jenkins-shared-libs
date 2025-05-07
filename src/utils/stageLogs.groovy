package utils

class HelloWorld implements Serializable {
    private String _solutionPath

    HelloWorld() {
        echo "hello"
    }

    void build() {
        IStepExecutor steps = ContextRegistry.getContext().getStepExecutor()

//        int returnStatus = steps.sh("echo \"building ${this._solutionPath}...\"")
        int returnStatus = steps.sh("${this._solutionPath}")
        if (returnStatus != 0) {
            steps.error("Unable To Execute The Command")
        }
    }
}
