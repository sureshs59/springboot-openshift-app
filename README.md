# Spring Boot OpenShift Application

A simple Spring Boot application configured for OpenShift deployment.

## Features

✅ RESTful API for Product Management  
✅ Spring Boot 3.1.5  
✅ Spring Data JPA  
✅ H2 Database (in-memory)  
✅ Health Check & Actuator Endpoints  
✅ OpenShift Deployment Configuration  
✅ Docker Support  
✅ Kubernetes/OpenShift YAML manifests  
✅ Horizontal Pod Autoscaling (HPA)  
✅ Comprehensive Unit Tests  
✅ Liveness & Readiness Probes  

## Project Structure

```
springboot-openshift-app/
├── src/
│   ├── main/
│   │   ├── java/com/example/springboot/
│   │   │   ├── entity/           # JPA entities
│   │   │   ├── dto/              # Data transfer objects
│   │   │   ├── repository/       # Data access layer
│   │   │   ├── service/          # Business logic
│   │   │   ├── controller/       # REST endpoints
│   │   │   └── Application.java  # Main entry point
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application.yml
│   └── test/
│       └── java/com/example/springboot/
├── pom.xml
├── Dockerfile
├── Dockerfile.multistage
├── openshift-deployment.yaml
├── deploy.sh
├── undeploy.sh
└── README.md
```

## Prerequisites

- Java 11 or higher
- Maven 3.6+
- Docker
- OpenShift CLI (`oc`)
- Active OpenShift cluster

## Installation

### 1. Clone the Repository

```bash
git clone https://github.com/sureshs59/springboot-openshift-app.git
cd springboot-openshift-app
```

### 2. Build the Application

```bash
mvn clean package
```

### 3. Build Docker Image

```bash
docker build -t springboot-openshift-app:latest .
```

### 4. Deploy to OpenShift

```bash
./deploy.sh
```

Or manually:

```bash
# Create project
oc new-project springboot-openshift

# Apply deployment
oc apply -f openshift-deployment.yaml

# Check deployment status
oc rollout status deployment/springboot-app-deployment
```

## API Endpoints

### Health Checks

```bash
# Application health
GET /health

# Application info
GET /info
```

### Products

```bash
# Get all products
GET /api/products

# Get product by ID
GET /api/products/{id}

# Create product
POST /api/products
Content-Type: application/json
{
  "name": "Laptop",
  "description": "High performance laptop",
  "price": 999.99,
  "quantity": 10
}

# Update product
PUT /api/products/{id}
Content-Type: application/json
{
  "name": "Updated Laptop",
  "price": 899.99
}

# Delete product
DELETE /api/products/{id}

# Search products by name
GET /api/products/search?name=Laptop
```

## Running Tests

```bash
mvn test
```

## Configuration

### Environment Variables

```bash
# Java options
JAVA_OPTIONS="-Xmx512m -Xms256m"

# Spring profiles
spring.profiles.active=prod
```

### Application Properties

Edit `src/main/resources/application.properties`:

```properties
spring.application.name=springboot-openshift-app
server.port=8080
server.servlet.context-path=/api
```

## OpenShift Deployment Details

### Resources Included

1. **Service**: ClusterIP service for internal communication
2. **Deployment**: Managed pod replicas
3. **Route**: External access to the application
4. **HorizontalPodAutoscaler**: Automatic scaling based on CPU/Memory
5. **Liveness Probe**: Container health check
6. **Readiness Probe**: Traffic readiness check

### Deployment Configuration

- **Replicas**: 2 (min), 5 (max) with HPA
- **Resources**:
  - Request: 256Mi memory, 250m CPU
  - Limit: 512Mi memory, 500m CPU
- **Probes**:
  - Liveness: 30s initial delay, 10s period
  - Readiness: 10s initial delay, 5s period

## Monitoring

### Check Deployment Status

```bash
# Check deployment
oc get deployment

# Check pods
oc get pods

# Check service
oc get svc

# Check route
oc get route
```

### View Logs

```bash
# View pod logs
oc logs -f pod-name

# View deployment logs
oc logs -f deployment/springboot-app-deployment
```

### Scale Application

```bash
# Manual scaling
oc scale deployment springboot-app-deployment --replicas=3

# Check HPA status
oc get hpa
```

## Troubleshooting

### Pod not starting

```bash
# Describe pod for details
oc describe pod pod-name

# Check logs
oc logs pod-name
```

### Service not accessible

```bash
# Check service
oc get svc

# Check endpoints
oc get endpoints

# Test connectivity
oc port-forward svc/springboot-app-service 8080:8080
```

### Resource issues

```bash
# Check resource usage
oc top nodes
oc top pods
```

## Cleanup

To remove the application from OpenShift:

```bash
./undeploy.sh
```

Or manually:

```bash
oc delete -f openshift-deployment.yaml
oc delete project springboot-openshift
```

## Docker Multi-stage Build

For optimized image size, use the multi-stage Dockerfile:

```bash
docker build -f Dockerfile.multistage -t springboot-openshift-app:latest .
```

## Running Locally

```bash
# Using Maven
mvn spring-boot:run

# Using JAR
java -jar target/springboot-openshift-app-1.0.0.jar

# Using Docker
docker run -p 8080:8080 springboot-openshift-app:latest
```

## Database Access

### H2 Console

Access at: `http://localhost:8080/api/h2-console`

- URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (leave blank)

## Performance Tuning

### JVM Options

```bash
JAVA_OPTIONS="-Xmx512m -Xms256m -XX:+UseG1GC -XX:+UnlockExperimentalVMOptions"
```

### Connection Pool

Edit `application.properties`:

```properties
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
```

## Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

MIT License

## Support

For issues or questions, please create a GitHub issue.

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [OpenShift Documentation](https://docs.openshift.com/)
- [Docker Documentation](https://docs.docker.com/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
