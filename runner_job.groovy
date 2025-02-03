import groovy.json.JsonSlurperClassic

timeout(40, timeunit="MINUTES") {
    node('maven') {

        stage('Send report to telegram') {
            withCredentials([secretText: 'telegram_chat_id', value: 'chatId'], 
            [secretText: 'bot_token', value: 'token']) {
                sh "curl -X POST -H 'Content-Type: application/json' -d '{\"chat_id\": \"$chatId\", \"text\": \"$(getTelegramReport()\"}' https://api.telegram.org/bot$token/sendMessage"
            }
        }
    }
} 

def getTelegramreport() {
    def path = './allure-report/export/influxDbData.txt'

    def fileContent = readFile path: path
    def fileContentLines = fileContent.split('\n')

    def result =[:]
    fileContentLines.each(line -> {
        def matcher = line =~ /(?i).*?(\w+)=(\d+).*/
        result[matcher[0][1]] = matcher[0][2]
    })

    def message = """============= REPORT =============
    REFSPEC: $env.REFSPEC
    TESTS_RUNNING: ${String.join(',', TESTS_LIST_RUNNING)}
    """

    result.each(k, v -> {
    message += "$k: $v\n"
    })

    return message
}

def getTelegramReportFromJson() {
    def path = "./allure-report/widget/summary.json"

    def summaryContent = readFile path: path
    def jsonReport = JsonSlurperClassic(). parseText(summaryContent)
    jsonReport = jsonReport['statistic']

    def message = """============= REPORT =============
    REFSPEC: $env.REFSPEC
    TESTS_RUNNING: ${String.join(',', TESTS_LIST_RUNNING)}
    """

    result.each(k, v -> {
    message += "$k: $v\n"
    })

    return message
}