USE [ShellPest]
GO
/****** Object:  StoredProcedure [dbo].[SP_BSC_ClienteGeneral]    Script Date: 25/08/2018 12:40:29 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF  EXISTS (SELECT * FROM SYS.OBJECTS WHERE TYPE = 'P' AND NAME = 'SP_Fertiliza_Insert')
DROP PROCEDURE SP_Fertiliza_Insert
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
create PROCEDURE [dbo].[SP_Fertiliza_Insert] 
	-- Add the parameters for the stored procedure here
	@Id_Fertiliza char(10),
	@Id_Huerta char(5),
	@Observaciones varchar(100),
	@Id_TipoAplicacion char(3),
	@Id_Presentacion char(4),
	@Id_Usuario varchar(10),
	@Anio char(2),
	@F_Usuario_Crea varchar(10),
	@c_codigo_eps char(2),
	@Centro_Costos varchar(100),
	@Ha_aplicadas numeric(18, 2)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	declare @correcto char(10)

	begin transaction T1;
	begin try


		declare @maximo char(10)
			select @maximo=concat(right(concat('000',count(A.Id_Fertiliza)+1),3),@Anio,@Id_Huerta) from t_Fertiliza as A
            left join (select datepart(year,min(ADT.Fecha)) as Fecha,
                        ADT.Id_Fertiliza 
                       from t_Fertiliza_Det as ADT 
                       group by ADT.Id_Fertiliza ) as AD on A.Id_Fertiliza=AD.Id_Fertiliza
            where rtrim(ltrim(A.Id_Huerta))=@Id_Huerta
					   and AD.Fecha =@Anio
			
				INSERT INTO dbo.t_Fertiliza
		           (Id_Fertiliza
		           ,Id_Huerta
		           ,Observaciones
				   ,Id_TipoAplicacion
				   ,Id_Presentacion
				   ,Id_Usuario_Crea
	           	   ,F_Usuario_Crea
				   ,c_codigo_eps
				   ,Centro_Costos
				   ,Ha_aplicadas)
		     	VALUES
		           (@maximo
		           ,@Id_Huerta
				   ,@Observaciones
				   ,@Id_TipoAplicacion
				   ,@Id_Presentacion
		           ,@Id_Usuario
	           	   ,@F_Usuario_Crea
				   ,@c_codigo_eps
				   ,@Centro_Costos
				   ,@Ha_aplicadas)
		
		commit transaction T1;
		set @correcto=@maximo
	end try
	begin catch
		rollback transaction T1;
		set @correcto=0
	end catch

	select @correcto resultado
END
