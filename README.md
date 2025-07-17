# 📂 TreeTrunk

> **A CLI and GUI tool that generates clean, exportable views of your project's file structure. Great for dev, docs, collaboration, or AI models**

---

## Installation (WIP)

### ✅ Developer Installers (Local Dev)

- **Windows**
```powershell
./install-treetrunk-dev.ps1
```

- **Linux / macOS**
```bash
./install-treetrunk-dev.sh
```

This will:
- Install TreeTrunk CLI to `~/.treetrunk` (Windows: `%USERPROFILE%\.treetrunk`)
- Add the folder to your **PATH** (or prompt you to do so)
- Provide a `treetrunk` command globally

> ℹ️ Production/public releases, on the way

---

## Building Locally

Ensure **Gradle** is installed.

To build the CLI:
```bash
./gradlew :cli:shadowJar
```

---

## CLI Usage

### Command Help
```bash
treetrunk --help
```

### Render the Current Directory
```bash
treetrunk render
```

### Render a Specific Directory
```bash
treetrunk render /path/to/your/project
```

### Customize Styles
```bash
treetrunk list-styles
```

To render with a style:
```bash
treetrunk render --style ASCII
treetrunk render --custom-style MY_STYLE
```

### Render Options
| Option                  | Description                          |
|-------------------------|--------------------------------------|
| `--max-depth <n>`       | Maximum depth to render             |
| `--max-children <n>`    | Max number of children per folder   |
| `--collapse-empty`      | Collapse chains of empty directories |
| `--forgive`             | Allow slight overflow on limits    |
| `--depth-forgiveness <n>` | Additional levels past max depth |
| `--child-forgiveness <n>` | Additional children per folder   |

### Output to File
```bash
treetrunk render --output /path/to/output.txt
```

---

### Example Output

```
project-root/
├── build.gradle
├── src/
│   ├── main/
│   │   └── Main.kt
│   └── test/
│       └── MainTest.kt
└── README.md
```

<details>
<summary>Output of this project as of 17/07/2025 (gradle exclude preset)</summary>

### Using:
`treetrunk render --e gradle`

```
./
└── ./
    ├── cli/
    │   ├── src/
    │   │   ├── main/
    │   │   │   ├── kotlin.com.glance.treetrunk.cli/
    │   │   │   │   ├── commands/
    │   │   │   │   │   ├── render/
    │   │   │   │   │   │   ├── AdvancedOptions.kt
    │   │   │   │   │   │   ├── OutputOptions.kt
    │   │   │   │   │   │   ├── RenderCommand.kt
    │   │   │   │   │   │   ├── RenderOptions.kt
    │   │   │   │   │   │   └── StrategyOptions.kt
    │   │   │   │   │   ├── HelperCommands.kt
    │   │   │   │   │   ├── ListStylesCommand.kt
    │   │   │   │   │   └── TreeTrunkCli.kt
    │   │   │   │   └── Main.kt
    │   │   │   └── resources
    │   │   └── test/
    │   │       ├── kotlin
    │   │       └── resources
    │   └── build.gradle.kts
    ├── core/
    │   ├── src/
    │   │   ├── main/
    │   │   │   ├── kotlin.com.glance.treetrunk.core/
    │   │   │   │   ├── config/
    │   │   │   │   │   ├── AdvancedConfig.kt
    │   │   │   │   │   ├── AppConfiguration.kt
    │   │   │   │   │   ├── RenderConfig.kt
    │   │   │   │   │   └── StrategyConfig.kt
    │   │   │   │   ├── strategy/
    │   │   │   │   │   ├── ignore/
    │   │   │   │   │   │   ├── parser/
    │   │   │   │   │   │   │   ├── GitIgnoreParser.kt
    │   │   │   │   │   │   │   ├── IgnoreResolver.kt
    │   │   │   │   │   │   │   └── TreeIgnoreParser.kt
    │   │   │   │   │   │   ├── rule/
    │   │   │   │   │   │   │   ├── GlobIgnoreRule.kt
    │   │   │   │   │   │   │   └── IgnoreRule.kt
    │   │   │   │   │   │   └── IgnoreEngine.kt
    │   │   │   │   │   ├── include/
    │   │   │   │   │   │   ├── parser/
    │   │   │   │   │   │   │   ├── IncludeResolver.kt
    │   │   │   │   │   │   │   └── TreeIncludeParser.kt
    │   │   │   │   │   │   ├── rule/
    │   │   │   │   │   │   │   ├── GlobIncludeRule.kt
    │   │   │   │   │   │   │   └── IncludeRule.kt
    │   │   │   │   │   │   ├── IncludeEngine.kt
    │   │   │   │   │   │   └── InclusionMode.kt
    │   │   │   │   │   ├── pattern/
    │   │   │   │   │   │   └── GlobPattern.kt
    │   │   │   │   │   ├── DepthMode.kt
    │   │   │   │   │   ├── Strategy.kt
    │   │   │   │   │   ├── StrategyFileParser.kt
    │   │   │   │   │   ├── StrategyFileParserRegistry.kt
    │   │   │   │   │   ├── StrategyLoader.kt
    │   │   │   │   │   └── StrategyRule.kt
    │   │   │   │   ├── tree/
    │   │   │   │   │   ├── model/
    │   │   │   │   │   │   └── TreeNode.kt
    │   │   │   │   │   ├── render/
    │   │   │   │   │   │   ├── text/
    │   │   │   │   │   │   │   ├── StyleRegistry.kt
    │   │   │   │   │   │   │   ├── TextRenderOpts.kt
    │   │   │   │   │   │   │   └── TextTreeRenderer.kt
    │   │   │   │   │   │   └── TreeRenderer.kt
    │   │   │   │   │   ├── CliTreeSymbols.kt
    │   │   │   │   │   ├── Defaults.kt
    │   │   │   │   │   └── TreeBuilder.kt
    │   │   │   │   └── util
    │   │   │   └── resources.strategy/
    │   │   │       ├── ignore/
    │   │   │       │   ├── defaults.treeignore
    │   │   │       │   └── gradle.trunkignore
    │   │   │       └── include/
    │   │   │           └── kotlin.trunkinclude
    │   │   └── test/
    │   │       ├── kotlin.com.glance.treetrunk.core.test.strategy/
    │   │       │   ├── ignore/
    │   │       │   │   └── GlobPatternTest.kt
    │   │       │   ├── include
    │   │       │   └── preset/
    │   │       │       └── StrategyLoaderTest.kt
    │   │       └── resources
    │   └── build.gradle.kts
    ├── gradle.wrapper/
    │   ├── gradle-wrapper.jar
    │   └── gradle-wrapper.properties
    ├── scripts.install/
    │   ├── install-treetrunk-dev.ps1
    │   └── install-treetrunk-dev.sh
    ├── .gitignore
    ├── README.md
    ├── build.gradle.kts
    ├── gradle.properties
    ├── gradlew
    ├── gradlew.bat
    └── settings.gradle.kts
```
</details>

---

## Strategies: Ignoring and Including Files

TreeTrunk supports **ignore/include rules** similar to `.gitignore`:

- Glob patterns like `*.txt` or `build/`
- Custom ignore/include files (e.g. `.treeignore`)
- Rule presets for ecosystems like **Gradle, Node, etc.** *(coming soon)*

### Strategy Options
| Option                    | Description                               |
|---------------------------|------------------------------------------|
| `--ignore <pattern>`      | Ignore items matching this pattern       |
| `--include <pattern>`     | Explicitly include items matching pattern|
| `--ignore-files <file>`   | File containing ignore rules             |
| `--include-files <file>`  | File containing include rules            |
| `--ignore-presets <name>` | Apply preset ignore rules                |
| `--use-local-ignores`     | Enable per-directory ignore files        |
| `--propagate-ignores`     | Pass ignores to subdirectories           |

---

## Developers

### Tree Building

To construct a directory tree programmatically:
```kotlin
val config = AppConfiguration.builder()
    .root(File("path/to/project"))
    .renderConfig(RenderConfig())
    .strategyConfig(StrategyConfig())
    .advancedConfig(AdvancedConfig())
    .build()

val tree = TreeBuilder.buildTree(config)
```

### Rendering

The rendering system is **pluggable and generic**:
```kotlin
interface TreeRenderer<Output, Options> {
    fun render(node: TreeNode, options: Options): Output
}
```

Example text rendering:
```kotlin
val renderOutput = TextTreeRenderer.render(tree, TextRenderOpts())
println(renderOutput)
```

### Creating a Custom Renderer

Implement:
```kotlin
class JsonTreeRenderer : TreeRenderer<JsonElement, JsonRenderOpts> { ... }
```

---

## Roadmap
- 🚧 JSON, Markdown, GUI renderers
- 🚧 Gradle/Maven plugin integrations
- 🚧 Public jar releases

---

## Contributing
Contributions are welcome! Planned:
- Developer guides
- Plugin hooks

---