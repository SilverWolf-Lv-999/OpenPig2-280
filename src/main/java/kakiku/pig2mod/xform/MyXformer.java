package kakiku.pig2mod.xform;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import cpw.mods.modlauncher.api.ITransformer.Target;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class MyXformer implements ITransformer {
   public @NotNull Set targets() {
      Set<ITransformer.Target> targets = new HashSet();
      targets.add(Target.targetClass("net/minecraft/client/Minecraft"));
      return targets;
   }

   public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
      return TransformerVoteResult.YES;
   }

   public @NotNull ClassNode transform(ClassNode classNode, ITransformerVotingContext context) {
      if (classNode.name.equals("net/minecraft/client/Minecraft")) {
         for(MethodNode method : classNode.methods) {
            if (method.name.equals("m_91341_") && method.desc.equals("()V")) {
            }
         }
      }

      return classNode;
   }
}

