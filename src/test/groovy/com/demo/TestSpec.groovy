import com.demo.Service
import com.demo.Resource
import spock.lang.Specification

class HelloSpockSpec extends Specification {
    def "length of Spock's and his friends' names"() {
        expect:
        name.size() == length

        where:
        name     | length
        "Spock"  | 5
        "Kirk"   | 4
        "Scotty" | 6
    }

    Service service = Mock()
    Resource resource = new Resource(service)

    def "can mock an implementation class"() {
        when:
            resource.execute()
        then:
            1 == 1
    }
}