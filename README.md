# Errorping-example

This project is a sample Spring Boot application that demonstrates how to use Errorping in a real-world setup.

It showcases:
- Global exception handling
- Logging behavior

## Requirements

- JDK 17+
- Gradle
- Discord Webhook URL

## Getting Started

### 1. Clone repositories

```bash
git clone https://github.com/jhssong/errorping-example.git
```

### 2. (Optional) Install Errorping Locally

You can optionally install the Errorping library into your local Maven repository if you want to work with the latest source version.

```bash
git clone https://github.com/jhssong/errorping.git
cd errorping
./gradlew publishToMavenLocal
```

## Configuration

### Discord Alert (Optional)

To enable Discord alerting, add the following to `application.yml`:
```yaml
errorping:
  discord-webhook-url: ${DISCORD_WEBHOOK_URL}
```

Then set the environment variables:
```bash
export DISCORD_WEBHOOK_URL=https://discord.com/api/webhooks/...
```

## Example

Once the application is running, open your browser and navigate to:

http://localhost:8080

The example project provides a simple web interface that allows you to
trigger various exception scenarios and observe Errorping’s behavior directly,
including error responses, logs, and alert behavior.
