import { ContentCopyRounded } from "@mui/icons-material";
import React, { useRef } from "react";
import { useEffect, useState } from "react";

const MyParticles = (props) => {

  const {
    width = "100%",
    height = "100%",
    background= "linear-gradient(to bottom, #b6fefd, #001f3f)",
    ...rest
  } = props;

  const canvasRef = useRef(null);
  const [context, setContext] = useState('');
  const [odlX, setOdlX] = useState(0);
  const [odlY, setOdlY] = useState(0);
  const step = 0.7;
  
  var order = 0;
  var frameCount = 0;
  var wait = false;
  var small = 8;

  const points = [
    [odlX, odlY],
    [2*odlX, 2*odlY],
    [3*odlX, odlY],
    [4*odlX, 2*odlY],
    [5*odlX, 1.5*odlY]
    
  ];
  
  const sizes = [small, 1.25*small, 0.75*small, small,1.25*small];
  
  const draw = (frameCount) => {

      const halfwidth = points[order][0];
      const halfheight =  points[order][1];
      const lineWidth = sizes[order];
      const pythonlength = 2*lineWidth;
      const between = 0.75 * lineWidth;
      context.lineWidth = lineWidth;
      
      if (wait) {
        let wyzn = halfheight - pythonlength;
        if (frameCount > halfheight + 0.5 * pythonlength) {
  
          context.strokeStyle = "white";
          context.beginPath();
          context.moveTo(halfwidth, halfheight + lineWidth / 2);
          context.lineTo(halfwidth + between, halfheight + lineWidth / 2);
          context.lineWidth = 1;
          context.stroke();

          context.beginPath();
          context.moveTo(halfwidth, halfheight - lineWidth / 2);
          context.lineTo(halfwidth - between, halfheight - lineWidth / 2);
          context.stroke();

          context.strokeStyle = "#FFD700";
          context.beginPath();
          context.moveTo(halfwidth, halfheight + lineWidth / 2 + 1);
          context.lineTo(halfwidth + between, halfheight + lineWidth / 2 + 1);
          context.lineWidth = 1;
          context.stroke();

          context.strokeStyle = "#6495ED";
          context.beginPath();
          context.moveTo(halfwidth, halfheight - lineWidth / 2 - 1);
          context.lineTo(halfwidth - between, halfheight - lineWidth / 2 - 1);
          context.stroke();

        } else if (frameCount > wyzn) {

          context.strokeStyle = "#FFD700";
          context.beginPath();
          context.moveTo(halfwidth - between, halfheight + context.lineWidth / 2);
          context.lineTo(halfwidth + between, halfheight + context.lineWidth / 2);
          context.stroke();

          context.strokeStyle = "#6495ED";
          context.beginPath();
          context.moveTo(halfwidth - between, halfheight - context.lineWidth / 2);
          context.lineTo(halfwidth + between, halfheight - context.lineWidth / 2);
          context.stroke();

          if (frameCount < wyzn + 2 * context.lineWidth) {
            context.fillStyle = "white";

            //zolty
            context.beginPath();
            context.arc(
              halfwidth - between + context.lineWidth / 4,
              frameCount + 0.75 * context.lineWidth,
              1,
              0,
              2 * Math.PI
            );
            context.fill();

            //niebieski
            context.beginPath();
            context.arc(
              halfwidth + between - context.lineWidth / 4,
              halfheight -
                context.lineWidth / 4 -
                (frameCount - halfheight + 0.5 * context.lineWidth),
              1,
              0,
              2 * Math.PI
            );
            context.fill();
          } else {

            //to jest oczko przesuwajace sie w prawo
            context.fillStyle = "white";
            context.beginPath();
            context.arc(
              halfwidth -
                between +
                context.lineWidth / 4 +
                frameCount -
                (wyzn + 2.0 * context.lineWidth),
              wyzn + 2.75 * context.lineWidth,
              1,
              0,
              2 * Math.PI
            );
            context.fill();

            //to jest oczko przesuwajace sie w lewo
            context.fillStyle = "white";
            context.beginPath();
            context.arc(
              halfwidth +
                between -
                context.lineWidth / 4 -
                (frameCount - (wyzn + 2.0 * context.lineWidth)),
              halfheight - 0.75 * context.lineWidth,
              1,
              0,
              2 * Math.PI
            );
            context.fill();
          }
        } else {
          context.fillStyle = "white";
          context.beginPath();
          context.arc(
            halfwidth +
              between -
              (pythonlength - context.lineWidth / 2) -
              context.lineWidth / 4,
            frameCount + context.lineWidth / 2,
            1,
            0,
            2 * Math.PI
          );
          context.fill();

          context.beginPath();
          context.arc(
            halfwidth -
              between +
              (pythonlength - (context.lineWidth / 2 + 1)) -
              context.lineWidth / 4,
            halfheight - context.lineWidth / 4,
            1,
            0,
            2 * Math.PI
          );
          context.fill();
        }
      } else {
        if(order)
          context.clearRect(points[order-1][0]+1.75 *sizes[order-1], 0, context.canvas.width-points[order-1][0]+1.75 *sizes[order-1], context.canvas.height);
        else
          context.clearRect(0, 0, context.canvas.width, context.canvas.height);

        if (frameCount >= halfheight - pythonlength) {
          if (
            frameCount <= halfheight &&
            halfheight - frameCount > context.lineWidth / 2
          ) {  
            context.strokeStyle = "#FFD700";
            context.beginPath();
            context.moveTo(halfwidth + between, frameCount);
           
            context.lineTo(
              halfwidth + between,
              halfheight + context.lineWidth / 2
            );
            context.stroke();

            context.strokeStyle = "#6495ED";
            context.beginPath();
            context.moveTo(
              halfwidth - between,
              halfheight - context.lineWidth / 2
            );   
            context.lineTo(
              halfwidth - between + (pythonlength - (halfheight - frameCount)),
              halfheight - context.lineWidth / 2
            );
            context.stroke();

            context.strokeStyle = "#6495ED";
            context.beginPath();
            context.moveTo(
              halfwidth - between,
              halfheight*2 - frameCount
            );
            context.lineTo(
              halfwidth - between,
              halfheight - context.lineWidth / 2
            );
            context.stroke();

            context.strokeStyle = "#FFD700";
            context.beginPath();
            context.moveTo(
              halfwidth + between,
              halfheight + context.lineWidth / 2
            );
            context.lineTo(
              halfwidth + between - (pythonlength - (halfheight - frameCount)),
              halfheight + context.lineWidth / 2
            );
            context.stroke();

            context.fillStyle = "white";
            context.beginPath();
            context.arc(
              halfwidth +
                between -
                (pythonlength - (halfheight - frameCount)) +
                context.lineWidth / 4,

              halfheight + context.lineWidth / 4,
              1,
              0,
              2 * Math.PI
            );
            context.fill();

            context.beginPath();
            context.arc(
              halfwidth -
                between +
                (pythonlength - (halfheight - frameCount)) -
                context.lineWidth / 4,
              halfheight - context.lineWidth / 4,
              1,
              0,
              2 * Math.PI
            );
            context.fill();

            if (halfheight - (frameCount + step) <= context.lineWidth / 2) {
              wait = true;
            }
          }
        } else {
          //tutaj mozna zrobic tak ze one sie tak xd wymieniają   ctx.bezierCurveTo(150, 50, 350, 350, 400, 200); tą funkcją
          context.strokeStyle = "#6495ED";
          context.beginPath();
          context.moveTo(halfwidth - between, halfheight*2 - frameCount);
          context.lineTo(
            halfwidth - between,
            halfheight*2 - (frameCount + pythonlength)
          );
          context.stroke();

          //snakes are falling down
          context.strokeStyle = "#FFD700";
          context.beginPath();
          context.moveTo(halfwidth + between, frameCount);
          context.lineTo(halfwidth + between, frameCount + pythonlength);
          context.stroke();

          //eyes yellow
          context.fillStyle = "white";
          context.beginPath();
          context.arc(
            halfwidth + between - context.lineWidth / 4,
            frameCount + pythonlength - 5,
            1,
            0,
            2 * Math.PI
          );
          context.fill();

          //eyes blue
          context.beginPath();
          context.arc(
            halfwidth - between + context.lineWidth / 4,
            halfheight*2 - (frameCount + pythonlength) + 5,
            1,
            0,
            2 * Math.PI
          );
          context.fill();
        }
    }
  };

  useEffect(() => {
    if (canvasRef.current) {
      const canvas = canvasRef.current;
      const ctx = canvas.getContext("2d");
      setContext(ctx);
    }
  });

  useEffect(() => {

    let animationFrameId;
    
    if (context.canvas) {

      setOdlX(context.canvas.width/6);
      setOdlY(context.canvas.height/10);
      
      const render = () => {
        frameCount += step;
        if (frameCount > context.canvas.height ) {
          wait = false;
          frameCount = 0;
          if(order<4)
            order++;
          else  order = 0;
        }
        draw(frameCount);
        animationFrameId = window.requestAnimationFrame(render);
      };
      render();
    }
    return () => {
      window.cancelAnimationFrame(animationFrameId);
    };
  }, [draw, context]);

  return (
    <div>
    <canvas
      ref={canvasRef}
      {...props}
      style={{ position: "fixed", width, height, background, zIndex: -1 }}
    />
    </div>
  );
};

export default MyParticles;
