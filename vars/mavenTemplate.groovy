def call() {
  """
kind: Pod
spec:
  containers:
  - name: maven
    image: maven:3.8.6-openjdk-11
    imagePullPolicy: Always
    command:
    - cat
    tty: true
  - name: jnlp
    image: cloudbees/cloudbees-core-agent
    resources:
      requests:
        memory: "300Mi"
      limits:
        memory: "300Mi"
    securityContext:
      runAsUser: 0
 """   
}
