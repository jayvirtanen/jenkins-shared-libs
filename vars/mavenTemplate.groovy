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
    securityContext:
      runAsUser: 0
 """   
}
