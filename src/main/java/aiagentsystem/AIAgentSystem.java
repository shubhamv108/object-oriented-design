package aiagentsystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AIAgentSystem {

    public enum LLMModel {
        GEMINI_FLASH_15,
    }

    public class Tool {
        private String name;
        public String execute(String... arguments) {
            return null;
        }
    }

    public abstract class Resource<ResourceType> {
        private ResourceType data;
//        private List<String> imagePaths;
//        private List<String> videoPaths;
//        private List<String> textPaths;
    }

    public abstract class Context<ContextType> {
        private ContextType contextType;
    }

    /**
     * Isolated
     * Interoperable
     * Discoverable
     * Scalable
     */
    public class MCPServer {
        private final Set<String> tools = new HashSet<>();
        private final Set<String> systemPrompts = new HashSet<>();
        private final Set<String> userPrompts = new HashSet<>();
        private final Set<String> resources = new HashSet<>();
        private final Set<Context<?>> contexts = new HashSet<>();

        public Collection<String> getTools() {
            return tools;
        }

        public Collection<String> getResources() {
            return resources;
        }

        public Collection<Context<?>> getContext() {
            return contexts;
        }

        public Collection<String> getSystemPrompts() {
            return systemPrompts;
        }
        public Collection<String> getUserPrompts() {
            return userPrompts;
        }

        public String invoke(String tool, String... arguments) {
            return null;
        }
    }

    public class MCPClient {

    }

    public interface IAgent {
        String act(String prompt, String checkerPrompt, ArrayList<String> responses);
    }

    public abstract class AbstractBaseAgent implements IAgent {
        private String name;
        private LLMModel model;
        private String description;
        private String instructions;
        private final List<String> sharedState = new ArrayList<>();

    }
    public abstract class WorkflowAgent extends AbstractBaseAgent {}

    public class Agent extends AbstractBaseAgent {
        private List<MCPClient> mcpClients;

        public String act(String prompt) {
            return null;
        }

        public String act(String prompt, ArrayList<String> responses) {
            return null;
        }

        public boolean review(String response, String prompt, ArrayList<String> responses) {
            return false;
        }

        @Override
        public String act(String prompt, String checkerPrompt, ArrayList<String> responses) {
            // RecallUserPreference
            // SaveUserPreference
            return null;
        }
    }

    public class RobustLoopAgent extends AbstractBaseAgent {
        private String name;
        private String description;
        private Agent approvedAgent;
        private Agent critiqAgent;
        private String systemPrompt;
        private int maxIterations;

        public String act(String prompt, String checkerPrompt, ArrayList<String> responses) {
            return act(prompt, checkerPrompt, 0, responses);
        }

        public String act(String prompt, String checkerPrompt, int iterations, ArrayList<String> responses) {
            String response = approvedAgent.act(prompt, responses);
            if (iterations == maxIterations || approvedAgent.review(checkerPrompt, response, responses))
                return response;
            return act(prompt, checkerPrompt, iterations + 1, responses);
        }
    }

    public class SequentialAgent extends AbstractBaseAgent {
        private String name;
        private final List<IAgent> subAgents = new LinkedList<>();
        private String description;

        @Override
        public String act(String prompt, String checkerPrompt, ArrayList<String> responses) {
            for (IAgent agent : subAgents)
                responses.add(agent.act(prompt, checkerPrompt, responses));
            return responses.getLast();
        }
    }

    public class SequentialParallelAgent extends AbstractBaseAgent {
        private String name;
        private final List<IAgent> subAgents = new LinkedList<>();
        private String description;

        @Override
        public String act(String prompt, String checkerPrompt, ArrayList<String> responses) {
            return "";
        }
    }

    public class ParallelAgent extends AbstractBaseAgent {
        private String name;
        private final List<IAgent> subAgents = new LinkedList<>();
        private String description;

        @Override
        public String act(String prompt, String checkerPrompt, ArrayList<String> responses) {
            List<CompletableFuture<String>> futures = subAgents.stream()
                    .map(agent -> CompletableFuture.supplyAsync(() -> agent.act(prompt, checkerPrompt, responses)))
                    .toList();

            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                    .thenApply(v -> futures.stream()
                            .map(CompletableFuture::join)
                            .collect(Collectors.joining("\n"))) // Combines strings cleanly
                    .join();
        }
    }

    public class CoordinatorHierarchicalTaskDecompositionRouterAgent extends AbstractBaseAgent {

        private String name;
        private final List<Agent> subAgents = new ArrayList<>();
        private String instructions;

        @Override
        public String act(String prompt, String checkerPrompt, ArrayList<String> responses) {
            return "";
        }
    }

    public class AgentTool implements IAgent {

        private IAgent agent;

        @Override
        public String act(String prompt, String checkerPrompt, ArrayList<String> responses) {
            return "";
        }
    }

    public class AgentAsTool implements IAgent {

        private String name;
        private final List<AgentTool> subAgents = new ArrayList<>();
        private String instructions;

        private List<String> sharedState = new ArrayList<>();

        @Override
        public String act(String prompt, String checkerPrompt, ArrayList<String> responses) {
            return "";
        }
    }

    public class Session {
        private final String appName;
        private final String userId;
        private final String sessionId;

        public Session(String appName, String userId, String sessionId) {
            this.appName = appName;
            this.userId = userId;
            this.sessionId = sessionId;
        }
    }

    public interface SessionService {
        Session getOrCreateSession(String appName, String userId, String sessionId);
    }

    public class InMemorySessionService implements SessionService {
        private final Map<String, Session> sessions = new HashMap<>();

        @Override
        public Session getOrCreateSession(String appName, String userId, String sessionId) {
            return null;
        }
    }
    public class DatabaseSessionService implements SessionService {
        @Override
        public Session getOrCreateSession(String appName, String userId, String sessionId) {
            return null;
        }
    }
    public class CloudSessionService implements SessionService {
        @Override
        public Session getOrCreateSession(String appName, String userId, String sessionId) {
            return null;
        }
    }

    public class UserProfile {
        private String userId;
        private Map<String, UserPreference> preferences = new HashMap<>();

        public Collection<UserPreference> recallUserPreferences() {
            return preferences.values();
        }
    }

    public class UserPreference {
        private String key;
        private String value;
    }

    public class RecallUserPreference extends AgentTool {}
    public class SaveUserPreference extends AgentTool {}

    public interface MemoryService {
        void addSessionToMemory(Session session);
        void addFile(String filePath);
    }

    public class InMemoryMemoryService implements MemoryService {
        @Override
        public void addSessionToMemory(Session session) {

        }

        @Override
        public void addFile(String filePath) {

        }
    }
    public class DatabaseMemoryService implements MemoryService {
        @Override
        public void addSessionToMemory(Session session) {

        }

        @Override
        public void addFile(String filePath) {

        }
    }
    public class CloudMemoryService implements MemoryService {
        private AgentEngine agentEngine;

        @Override
        public void addSessionToMemory(Session session) {

        }

        @Override
        public void addFile(String filePath) {

        }
    }

    public class AgentEngine {
        private LLMModel generatingModel;
        private LLMModel embeddingModel;
        private ArrayList<String> topics = new ArrayList<>();
    }

    public class PreloadMemoryTool extends AgentTool {

    }
}
